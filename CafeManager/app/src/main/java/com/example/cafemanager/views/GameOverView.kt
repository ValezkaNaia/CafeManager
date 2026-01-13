package com.example.cafemanager.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cafemanager.R
import com.example.cafemanager.navigation.Routes

/**
 * Ecrã de Game Over
 * - Mostra a imagem de fundo do ecrã de derrota (gameover.png) a ocupar todo o ecrã.
 * - Sobrepõe o botão "Play Again" (bttnplayagain.png) que, quando clicado,
 *   navega de volta para a Home para o utilizador poder começar novamente.
 */
@Composable
fun GameOverView(
    navController: NavController // Necessário para navegar de volta para a Home
) {
    // Caixa base que ocupa 100% do ecrã
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagem de fundo do Game Over
        Image(
            painter = painterResource(id = R.drawable.gameover), // Recurso da imagem de fundo
            contentDescription = "Game Over Background", // Descrição para acessibilidade
            modifier = Modifier.fillMaxSize(), // Preenche todo o ecrã
            contentScale = ContentScale.Crop // Corta/expande para preencher mantendo proporção
        )

        // Camada para posicionar elementos por percentagens do ecrã
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val w = maxWidth // Largura disponível
            val h = maxHeight // Altura disponível
            // Função auxiliar: posiciona e dimensiona um elemento por percentagens
            fun Modifier.boxAt(
                xPercent: Float, yPercent: Float, // posição (top-left)
                widthPercent: Float, heightPercent: Float // tamanho
            ) = this
                .align(Alignment.TopStart)
                .offset(x = w * xPercent, y = h * yPercent)
                .size(width = w * widthPercent, height = h * heightPercent)

            // Botão "Play Again" como imagem clicável
            Image(
                painter = painterResource(id = R.drawable.bttnplayagain), // Asset do botão
                contentDescription = "Play Again", // Descrição acessível
                modifier = Modifier
                    .boxAt(0.25f, 0.75f, 0.5f, 0.09f) // posição/tamanho por percentagens
                    .clickable {
                        // Navega para a Home e remove ecrãs anteriores da pilha até à Home
                        navController.navigate(Routes.HOME_SCREEN) {
                            popUpTo(Routes.HOME_SCREEN) { inclusive = true }
                        }
                    }
            )
        }
    }
}
