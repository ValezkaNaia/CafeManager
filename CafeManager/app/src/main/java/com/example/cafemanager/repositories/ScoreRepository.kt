package com.example.cafemanager.repositories

import com.example.cafemanager.models.PlayerScore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Repositório de Scores (placar/classificações)
 * - add(score): adiciona um novo documento à coleção "scores".
 * - getTopScores(limit): lê os melhores scores ordenados por "score" desc.
 *
 * Nota: Atualmente não é usado no fluxo principal, mas fica preparado para futuro.
 */
class ScoreRepository @Inject constructor(
    private val db: FirebaseFirestore // Instância do Firestore injetada via Hilt
) {

    /**
     * Adiciona um score à coleção "scores".
     * Emite Loading → Success ou Error com mensagem.
     */
    fun add(score: PlayerScore): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())
            db.collection("scores")
                .add(score)
                .await()
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Obtém os top N scores, ordenados descendentemente pelo campo "score".
     */
    fun getTopScores(limit: Long = 10): Flow<ResultWrapper<List<PlayerScore>>> = flow {
        try {
            emit(ResultWrapper.Loading())
            val snapshot = db.collection("scores")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            // Converte documentos para PlayerScore, preenchendo o docId com o id do documento
            val scores = snapshot.documents.mapNotNull { doc ->
                val data = doc.toObject(PlayerScore::class.java)
                data?.apply { docId = doc.id }
            }
            emit(ResultWrapper.Success(scores))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)
}