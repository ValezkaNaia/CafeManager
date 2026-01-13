package com.example.cafemanager.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafemanager.models.PlayerScore
import com.example.cafemanager.repositories.ScoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * ViewModel do ecrã de Game Over
 * - Responsável por persistir o score do jogador quando o jogo termina.
 * - Requer que o utilizador esteja autenticado (usa FirebaseAuth para obter o UID).
 * - Usa o ScoreRepository para escrever no Firestore.
 */
@HiltViewModel
class GameOverViewModel @Inject constructor(
    private val auth: FirebaseAuth,               // Cliente de autenticação (para obter utilizador atual)
    private val scoreRepository: ScoreRepository, // Repositório para guardar scores no Firestore
    private val authRepository: com.example.cafemanager.repositories.AuthRepository // Atualiza stats no perfil
) : ViewModel() {

    /**
     * Guarda o score do utilizador autenticado e atualiza highestScore/highestLevel se forem maiores.
     * - Se não houver utilizador autenticado, a função retorna imediatamente.
     */
    fun saveScoreIfLoggedIn(score: Int, level: Int) {
        val user = auth.currentUser ?: return // Sem sessão → não guarda
        val playerScore = PlayerScore(
            userId = user.uid,
            playerName = user.displayName ?: user.email,
            score = score,
        )
        viewModelScope.launch {
            // 1) Adiciona ao placar (coleção "scores")
            scoreRepository.add(playerScore).collect { /* ignorar estados por agora */ }
            // 2) Atualiza estatísticas do perfil no Firestore
            authRepository.updateUserStatsIfHigher(score, level).collect { /* ignorar estados por agora */ }
        }
    }
}
