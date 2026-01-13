package com.example.cafemanager.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafemanager.repositories.AuthRepository
import com.example.cafemanager.repositories.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * ViewModel do ecrã de Registo
 * - Mantém o estado dos inputs (username, email, password), erros e loading.
 * - Faz validação local antes de chamar o repositório.
 * - Chama o AuthRepository para criar o utilizador no Auth e o perfil no Firestore.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository // Repositório com operações de Auth + Firestore
) : ViewModel() {

    // Estado interno mutável do ecrã de registo
    private val _uiState = mutableStateOf(RegisterUiState())
    // Exposição de estado apenas de leitura para a UI
    val uiState: State<RegisterUiState> = _uiState

    /** Atualiza o username e limpa erros associados */
    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username.trim(),
            usernameError = null,
            generalError = null
        )
    }

    /** Atualiza o email e limpa erros associados */
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email.trim(),
            emailError = null,
            generalError = null
        )
    }

    /** Atualiza a palavra‑passe e limpa erros associados */
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            generalError = null
        )
    }

    /**
     * Executa o fluxo de registo completo:
     * - Valida inputs localmente.
     * - Evita chamadas duplicadas quando em loading.
     * - Chama o repositório para criar Auth user + documento em Firestore (users/{uid}).
     * - Atualiza o estado com Loading/Success/Error e notifica a UI em caso de sucesso.
     */
    fun register(onRegistered: () -> Unit) {
        if (!validateInputs()) return
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            authRepository.registerAndCreateProfile(
                username = _uiState.value.username,
                email = _uiState.value.email,
                password = _uiState.value.password
            ).collect { result ->
                when (result) {
                    is ResultWrapper.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, generalError = null)
                    }
                    is ResultWrapper.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        onRegistered()
                    }
                    is ResultWrapper.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, generalError = result.message)
                    }
                }
            }
        }
    }

    /**
     * Valida inputs do registo (obrigatoriedade + formato de email + tamanho mínimo da password).
     * Retorna true se todos os campos estiverem válidos.
     */
    private fun validateInputs(): Boolean {
        var usernameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null

        val username = _uiState.value.username
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (username.isBlank()) usernameError = "Username é obrigatório"
        if (email.isBlank()) {
            emailError = "Email é obrigatório"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Formato de email inválido"
        }
        if (password.isBlank()) {
            passwordError = "Palavra‑passe é obrigatória"
        } else if (password.length < 6) {
            passwordError = "Usa pelo menos 6 caracteres"
        }

        _uiState.value = _uiState.value.copy(
            usernameError = usernameError,
            emailError = emailError,
            passwordError = passwordError,
            generalError = null
        )
        return usernameError == null && emailError == null && passwordError == null
    }
}
