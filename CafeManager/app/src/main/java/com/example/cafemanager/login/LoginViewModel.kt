package com.example.cafemanager.login

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
 * ViewModel do ecrã de Login
 * - Mantém e atualiza o estado da UI (email, password, erros, loading).
 * - Faz validação simples no cliente antes de chamar o repositório.
 * - Orquestra chamadas ao AuthRepository e traduz resultados para o estado da UI.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository // Repositório responsável por operações de autenticação
) : ViewModel() {

    // Estado interno mutável (privado)
    private val _uiState = mutableStateOf(LoginUiState())
    // Exposição de estado apenas de leitura para a UI
    val uiState: State<LoginUiState> = _uiState

    /**
     * Atualiza o campo de email e limpa erros associados.
     */
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email.trim(), // Remove espaços extra
            emailError = null,    // Limpa erro específico do email
            generalError = null   // Limpa erro geral quando o utilizador volta a editar
        )
    }

    /**
     * Atualiza o campo de password e limpa erros associados.
     */
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            generalError = null
        )
    }

    /**
     * Efetua o login do utilizador.
     * - Valida inputs localmente.
     * - Evita chamadas duplicadas quando está em loading.
     * - Coleciona o fluxo do repositório e entrega para tratamento comum.
     */
    fun login(onAuthSuccess: () -> Unit) {
        if (!validateInputs(isRegister = false)) return
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            authRepository.login(_uiState.value.email, _uiState.value.password).collect { result ->
                handleAuthResult(result, onAuthSuccess)
            }
        }
    }

    /**
     * Registo simples (email+password) – atualmente não usado porque o registo completo
     * está no ecrã Register com username. Mantido por conveniência.
     */
    fun register(onAuthSuccess: () -> Unit) {
        if (!validateInputs(isRegister = true)) return
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            authRepository.register(_uiState.value.email, _uiState.value.password).collect { result ->
                handleAuthResult(result, onAuthSuccess)
            }
        }
    }

    /**
     * Valida os campos localmente (formato de email e tamanho mínimo da password no registo).
     * Retorna true se estiver tudo válido.
     */
    private fun validateInputs(isRegister: Boolean): Boolean {
        var emailError: String? = null
        var passwordError: String? = null
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank()) {
            emailError = "Email é obrigatório"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Formato de email inválido"
        }

        if (password.isBlank()) {
            passwordError = "Palavra‑passe é obrigatória"
        } else if (isRegister && password.length < 6) {
            passwordError = "Usa pelo menos 6 caracteres"
        }

        _uiState.value = _uiState.value.copy(
            emailError = emailError,
            passwordError = passwordError,
            generalError = null
        )
        return emailError == null && passwordError == null
    }

    /**
     * Trata os resultados do repositório e atualiza o estado da UI de forma consistente.
     */
    private fun handleAuthResult(result: ResultWrapper<Unit>, onAuthSuccess: () -> Unit) {
        when (result) {
            is ResultWrapper.Loading -> {
                _uiState.value = _uiState.value.copy(isLoading = true, generalError = null)
            }
            is ResultWrapper.Success -> {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onAuthSuccess() // Notifica a UI para navegar/atualizar após sucesso
            }
            is ResultWrapper.Error -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    generalError = result.message // Mostra mensagem curta e amigável
                )
            }
        }
    }
}