package com.example.cafemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cafemanager.login.LoginScreen
import com.example.cafemanager.navigation.Routes
import com.example.cafemanager.ui.theme.CafeManagerTheme
import com.example.cafemanager.views.GameOverView
import com.example.cafemanager.views.GameScreen
import com.example.cafemanager.views.HomeView
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal da aplicação.
 * - Marcada com @AndroidEntryPoint para permitir injeção de dependências com Hilt.
 * - Cria o conteúdo Compose e arranca a árvore de navegação.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * onCreate: ciclo de vida inicial da Activity.
     * - Chama setContent para desenhar a UI em Compose.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Arranca a composição principal da app (tema + NavHost)
            CafeManagerApp()
        }
    }
}

/**
 * Função composable raiz da aplicação.
 * - Aplica o tema da app.
 * - Cria um NavController para gerir a navegação entre ecrãs.
 * - Define o NavHost com os destinos/rotas disponíveis.
 */
@Composable
fun CafeManagerApp() {
    // Aplica o tema Material3 com a tipografia personalizada
    CafeManagerTheme {
        // Controlador de navegação em memória
        val navController = rememberNavController()
        // NavHost: ponto central da navegação; define o ecrã inicial e as rotas
        NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {
            // Rota da Home
            composable(Routes.HOME_SCREEN) {
                // Desenha o ecrã Home e passa o navController para ações de navegação
                HomeView(navController = navController)
            }
            // Rota do Login
            composable(Routes.LOGIN_SCREEN) {
                LoginScreen(
                    // Callback chamado quando o login tem sucesso → navegar para Home
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME_SCREEN) {
                            // Remove o ecrã de Login da pilha para não voltar para trás inadvertidamente
                            popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                        }
                    },
                    // Ir para o ecrã de Registo
                    onGoToRegister = { navController.navigate(Routes.REGISTER_SCREEN) }
                )
            }
            // Rota do Registo
            composable(Routes.REGISTER_SCREEN) {
                com.example.cafemanager.register.RegisterScreen(
                    onRegistered = {
                        // Após registo com sucesso → navegar para Home
                        navController.navigate(Routes.HOME_SCREEN) {
                            // Garantir que o Login fica fora da pilha
                            popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                        }
                    },
                    // Voltar para o ecrã de Login
                    onGoToLogin = { navController.popBackStack() }
                )
            }
            // Rota do Jogo
            composable(Routes.GAME_SCREEN) {
                GameScreen(navController = navController)
            }
            // Rota do Game Over
            composable(Routes.GAME_OVER_SCREEN) {
                GameOverView(navController = navController)
            }
        }
    }
}