package com.example.cafemanager.models

/**
 * Perfil do utilizador guardado em Firestore na coleção `users/{uid}`.
 */
data class UserProfile(
    val username: String = "",
    val email: String = "",
    val highestScore: Int = 0,
    val highestLevel: Int = 0,
    val createdAt: com.google.firebase.Timestamp? = null
)