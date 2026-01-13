package com.example.cafemanager.models

import java.util.Date

/**
 * Modelo de dados para representar um score de jogador.
 * - docId: id do documento no Firestore (preenchido após leitura/gravação).
 * - userId: UID do utilizador no Firebase Auth a quem pertence o score.
 * - playerName: nome a mostrar (displayName ou email como fallback).
 * - score: valor numérico do score.
 */
data class PlayerScore(
    var docId: String? = null,   // ID do documento no Firestore
    var userId: String? = null,  // UID do utilizador
    var playerName: String? = null, // Nome do jogador
    var score: Int = 0,          // Valor do score
)