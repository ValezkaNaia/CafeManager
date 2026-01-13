package com.example.cafemanager.repositories

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.example.cafemanager.models.UserProfile
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Repositório de Autenticação e Perfil
 * - Encapsula o acesso ao Firebase Auth e ao Firestore relacionado com perfis de utilizador.
 * - Expõe operações como login, registo, leitura/atualização do perfil em `users/{uid}`.
 * - Todas as funções devolvem um Flow<ResultWrapper<...>> para fácil integração com a UI.
 */
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth, // Cliente de autenticação do Firebase
    private val firestore: com.google.firebase.firestore.FirebaseFirestore // Cliente do Firestore
) {

    /**
     * Inicia sessão com email e palavra‑passe.
     * Emite Loading → Success ou Error com mensagem amigável.
     */
    fun login(email: String, password: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            auth.signInWithEmailAndPassword(email, password).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(mapAuthException(e)))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Regista novo utilizador só no Auth (sem perfil). Mantido por conveniência.
     */
    fun register(email: String, password: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            auth.createUserWithEmailAndPassword(email, password).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(mapAuthException(e)))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Regista no Auth e cria documento de perfil no Firestore em `users/{uid}`.
     * Campos guardados: username, email, highestScore=0, highestLevel=0, createdAt=serverTimestamp.
     */
    fun registerAndCreateProfile(username: String, email: String, password: String): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            // 1) Cria o utilizador no Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw IllegalStateException("Falha ao obter UID do utilizador")
            // 2) Constrói o documento de perfil
            val userDoc = hashMapOf(
                "username" to username,
                "email" to email,
                "highestScore" to 0,
                "highestLevel" to 0,
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
            // 3) Escreve no Firestore em users/{uid}
            firestore.collection("users").document(uid).set(userDoc).await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            // Converte exceções de Auth/Firestore em mensagens curtas para a UI
            emit(ResultWrapper.Error(mapAuthException(e)))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Atualiza as estatísticas do utilizador autenticado (highestScore/highestLevel)
     * apenas se os novos valores forem superiores aos existentes.
     * Usa uma transação para garantir consistência.
     */
    fun updateUserStatsIfHigher(newScore: Int, newLevel: Int): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val uid = auth.currentUser?.uid ?: throw IllegalStateException("Não autenticado")
            val docRef = firestore.collection("users").document(uid)
            firestore.runTransaction { tx ->
                val snap = tx.get(docRef)
                val currentScore = (snap.getLong("highestScore") ?: 0L).toInt()
                val currentLevel = (snap.getLong("highestLevel") ?: 0L).toInt()
                val updates = hashMapOf<String, Any>()
                if (newScore > currentScore) updates["highestScore"] = newScore
                if (newLevel > currentLevel) updates["highestLevel"] = newLevel
                if (updates.isNotEmpty()) {
                    tx.update(docRef, updates as Map<String, Any>)
                }
            }.await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(mapAuthException(e)))
        }
    }.flowOn(Dispatchers.IO)

    /** Lê o perfil do utilizador autenticado de `users/{uid}`. */
    fun getCurrentUserProfile(): Flow<ResultWrapper<UserProfile>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val uid = auth.currentUser?.uid ?: throw IllegalStateException("Não autenticado")
            val snap = firestore.collection("users").document(uid).get().await()
            val profile = snap.toObject(UserProfile::class.java)
                ?: throw IllegalStateException("Perfil não encontrado")
            emit(ResultWrapper.Success(profile))
        } catch (e: Exception) {
            val msg = when (e) {
                is FirebaseFirestoreException -> if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) "Sem permissão" else (e.message ?: "Erro Firestore")
                else -> e.message ?: "Erro inesperado"
            }
            emit(ResultWrapper.Error(msg))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Mapeia várias exceções do Firebase para mensagens simples e localizadas.
     */
    private fun mapAuthException(e: Exception): String {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                // Email mal formatado ou password incorreta consoante a operação
                "Credenciais inválidas"
            }
            is FirebaseAuthInvalidUserException -> {
                "Conta inexistente ou desativada"
            }
            is FirebaseAuthUserCollisionException -> {
                "Email já em uso"
            }
            is FirebaseAuthWeakPasswordException -> {
                "Palavra‑passe fraca (≥6)"
            }
            is FirebaseNetworkException -> {
                "Sem internet"
            }
            else -> {
                // Fallback: usa a mensagem original se existir; caso contrário, genérica
                e.message?.takeIf { it.isNotBlank() } ?: "Erro inesperado"
            }
        }
    }
}
