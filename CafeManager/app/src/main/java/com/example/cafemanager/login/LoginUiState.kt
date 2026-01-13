package com.example.cafemanager.login 

/**
 * Estrutura de estado para o ecrã de Login.
 * - Guarda os valores introduzidos e erros de validação, bem como estado de loading.
 */
data class LoginUiState(
    val isLoading: Boolean = false, // Indica se há uma operação em curso (ex.: autenticação)
    val email: String = "",        // Email introduzido pelo utilizador
    val password: String = "",     // Palavra‑passe introduzida
    val emailError: String? = null, // Mensagem de erro específica do campo email (ou null se OK)
    val passwordError: String? = null, // Mensagem de erro específica da palavra‑passe
    val generalError: String? = null // Mensagem de erro geral (ex.: vindo do backend/Firebase)
)