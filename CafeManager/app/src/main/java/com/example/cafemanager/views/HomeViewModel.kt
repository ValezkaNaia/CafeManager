package com.example.cafemanager.views

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel da Home
 * - Gere o estado simples "está autenticado?" para decidir que botões mostrar.
 * - Observa alterações de sessão do FirebaseAuth para atualizar a UI automaticamente.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth // Dependência do Firebase Auth injetada pelo Hilt
) : ViewModel() {

    // Estado interno (privado) que guarda se existe utilizador autenticado
    private val _isLoggedIn = mutableStateOf(auth.currentUser != null)
    // Exposição do estado de apenas-leitura para a UI
    val isLoggedIn: State<Boolean> = _isLoggedIn

    init {
        // Listener do FirebaseAuth: sempre que a sessão muda, atualiza o estado
        auth.addAuthStateListener { firebaseAuth ->
            _isLoggedIn.value = firebaseAuth.currentUser != null
        }
    }

    /**
     * Faz logout do utilizador atual.
     * - Chama signOut() no FirebaseAuth.
     * - Atualiza o estado local.
     * - Executa um callback para a UI, caso exista lógica adicional.
     */
    fun logout(onLogoutSuccess: () -> Unit) {
        auth.signOut()
        _isLoggedIn.value = false
        onLogoutSuccess()
    }
}
