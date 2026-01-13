package com.example.cafemanager.profile

/**
 * Estado de UI para o ecrã de Perfil.
 */
data class ProfileUiState(
    val isLoading: Boolean = true,
    val username: String = "",
    val email: String = "",
    val highestScore: Int = 0,
    val highestLevel: Int = 0,
    val soundVolume: Float = 1.0f,
    val error: String? = null
)