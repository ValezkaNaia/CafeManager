package com.example.cafemanager.register

/**
 * Estrutura de estado para o ecrã de Registo.
 * - Guarda os inputs (username, email, password), erros por campo, erro geral e loading.
 */
data class RegisterUiState(
    val isLoading: Boolean = false, // Indica se há operação de registo em curso
    val username: String = "",     // Nome de utilizador
    val email: String = "",        // Email do utilizador
    val password: String = "",     // Palavra‑passe
    val usernameError: String? = null, // Erro específico do username
    val emailError: String? = null,    // Erro específico do email
    val passwordError: String? = null, // Erro específico da palavra‑passe
    val generalError: String? = null   // Erro geral (ex.: vindo do backend/Firebase)
)
