package com.example.cafemanager.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cafemanager.R

/**
 * Ecrã de Login
 * - Desenha a imagem de fundo (login.png) a ocupar o ecrã.
 * - Sobrepõe caixas de texto invisíveis (email e password) alinhadas com as caixas desenhadas no PNG.
 * - Mostra mensagens de erro compactas e um indicador de loading durante a autenticação.
 * - Inclui dois botões em forma de imagem: Login e Register.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,   // Callback para navegar/atualizar quando o login tem sucesso
    onGoToRegister: () -> Unit,   // Callback para ir para o ecrã de Registo
    viewModel: LoginViewModel = hiltViewModel() // ViewModel injetado via Hilt
) {
    // Observa o estado atual da UI (email/password/erros/loading)
    val uiState by viewModel.uiState

    // Contentor base a ocupar o ecrã inteiro
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagem de fundo do ecrã de login
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Login Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Corta para preencher mantendo proporção
        )

        // Camada com constraints para posicionamento percentual
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val w = maxWidth // Largura total disponível
            val h = maxHeight // Altura total disponível
            // Função auxiliar: posiciona/dimensiona um elemento por percentagens do ecrã
            fun Modifier.boxAt(
                xPercent: Float, yPercent: Float,
                widthPercent: Float, heightPercent: Float
            ) = this
                .align(Alignment.TopStart)
                .offset(x = w * xPercent, y = h * yPercent)
                .size(width = w * widthPercent, height = h * heightPercent)

            // Campo de Email (invisível a nível de fundo, mas com texto visível)
            TextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                isError = uiState.emailError != null,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .boxAt(0.12f, 0.36f, 0.76f, 0.10f)
            )
            // Erro específico do email (compacto)
            uiState.emailError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.boxAt(0.12f, 0.455f, 0.76f, 0.03f)
                )
            }

            // Campo de Password (invisível no fundo; texto oculto com •••)
            TextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                isError = uiState.passwordError != null,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .boxAt(0.12f, 0.48f, 0.76f, 0.10f)
            )
            // Erro específico da password (compacto)
            uiState.passwordError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.boxAt(0.12f, 0.575f, 0.76f, 0.03f)
                )
            }

            // Erro geral em forma de "pill" compacta
            uiState.generalError?.let {
                Box(
                    modifier = Modifier
                        .boxAt(0.12f, 0.64f, 0.76f, 0.04f)
                        .background(
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = it,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }

            // Indicador de progresso no centro quando em loading
            if (uiState.isLoading) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator()
                }
            }

            // Botão de Login (imagem clicável)
            Image(
                painter = painterResource(id = R.drawable.bttnloginlogin),
                contentDescription = "Login",
                modifier = Modifier
                    .boxAt(xPercent = 0.25f, yPercent = 0.72f, widthPercent = 0.5f, heightPercent = 0.09f)
                    .clickable(enabled = !uiState.isLoading) { viewModel.login(onLoginSuccess) }
            )
            // Botão para ir para Registo (imagem clicável)
            Image(
                painter = painterResource(id = R.drawable.bbtnregister),
                contentDescription = "Register",
                modifier = Modifier
                    .boxAt(xPercent = 0.25f, yPercent = 0.82f, widthPercent = 0.5f, heightPercent = 0.09f)
                    .clickable(enabled = !uiState.isLoading) { onGoToRegister() }
            )
        }
    }
}
