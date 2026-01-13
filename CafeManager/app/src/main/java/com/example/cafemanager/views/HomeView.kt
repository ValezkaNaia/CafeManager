package com.example.cafemanager.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cafemanager.R
import com.example.cafemanager.navigation.Routes

/**
 * Ecrã Home (Página inicial)
 * - Mostra a imagem de fundo completa (home.png).
 * - Sobrepõe dois botões feitos com imagens: Play e Login/Perfil.
 * - Quando o utilizador está autenticado, o botão "Login" muda para "Profile".
 */
@Composable
fun HomeView(
    navController: NavController, // Controlador de navegação para mudar de ecrã
    viewModel: HomeViewModel = hiltViewModel() // ViewModel injetado via Hilt para estado de autenticação
) {
    // Observa o estado de login a partir do ViewModel
    val isLoggedIn by viewModel.isLoggedIn

    // Música do menu principal
    val context = androidx.compose.ui.platform.LocalContext.current
    val menuMusic = com.example.cafemanager.utils.rememberLoopingMusic(resId = R.raw.mainmenu)
    androidx.compose.runtime.LaunchedEffect(Unit) { menuMusic.start() }

    // Contentor base a ocupar todo o ecrã
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagem de fundo da Home
        Image(
            painter = painterResource(id = R.drawable.home), // Recurso drawable da imagem de fundo
            contentDescription = "Homepage Background", // Descrição para acessibilidade
            modifier = Modifier.fillMaxSize(), // Preenche todo o espaço
            contentScale = ContentScale.Crop // Corta para preencher mantendo proporções
        )

        // Caixa com constraints do ecrã para calcular posições/tamanhos percentuais
        // As posições dos botões são definidas em percentagem de largura/altura do ecrã.
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val w: Dp = this.maxWidth // Largura disponível
            val h: Dp = this.maxHeight // Altura disponível

            // Função auxiliar para posicionar e dimensionar um elemento por percentagens
            fun Modifier.boxAt(
                xPercent: Float, yPercent: Float, // posição (superior-esquerda) em percentagem
                widthPercent: Float, heightPercent: Float // tamanho em percentagem
            ): Modifier {
                return this
                    .align(Alignment.TopStart) // Alinha na origem do ecrã
                    .offset(x = w * xPercent, y = h * yPercent) // Desloca pela percentagem
                    .size(width = w * widthPercent, height = h * heightPercent) // Define tamanho por percentagem
            }

            // Botão Play (imagem clicável)
            Image(
                painter = painterResource(id = R.drawable.bttnplay), // Asset do botão Play
                contentDescription = "Play", // Descrição para acessibilidade
                modifier = Modifier
                    .boxAt(xPercent = 0.25f, yPercent = 0.85f, widthPercent = 0.5f, heightPercent = 0.09f) // coloca por percentagens
                    .clickable {
                        com.example.cafemanager.utils.playSfx(context, R.raw.buttonclick)
                        navController.navigate(Routes.GAME_SCREEN)
                    } // Navega para o ecrã do jogo (placeholder)
            )

            if (!isLoggedIn) {
                // Quando não autenticado, mostrar botão Login
                Image(
                    painter = painterResource(id = R.drawable.bttnlogin),
                    contentDescription = "Login",
                    modifier = Modifier
                        .boxAt(xPercent = 0.25f, yPercent = 0.77f, widthPercent = 0.5f, heightPercent = 0.09f)
                        .clickable {
                            com.example.cafemanager.utils.playSfx(context, R.raw.buttonclick)
                            navController.navigate(Routes.LOGIN_SCREEN)
                        } // Navega para ecrã de Login
                )
            }
        }

        // Botão de Logout (texto simples) no topo-direito quando o utilizador está autenticado
        if (isLoggedIn) {
            Text(
                text = "Logout", // Texto do botão
                style = MaterialTheme.typography.bodyLarge, // Usa tipografia definida no tema (fonte personalizada)
                color = Color.White, // Cor branca para visibilidade sobre a imagem
                modifier = Modifier
                    .align(Alignment.TopEnd) // Posiciona no canto superior direito
                    .padding(16.dp) // Margem para não colar às bordas
                    .clickable {
                        com.example.cafemanager.utils.playSfx(context, R.raw.buttonclick)
                        viewModel.logout { /* Mantém no ecrã Home após logout */ }
                    } // Chama logout no ViewModel
            )
        }
    }
}
