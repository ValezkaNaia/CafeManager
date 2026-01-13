package com.example.cafemanager.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cafemanager.R
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Ecrã de Registo
 * - Fundo: imagem register.png a preencher o ecrã.
 * - Inputs invisíveis (username, email, password) alinhados com as caixas do PNG.
 * - Mensagens de erro compactas e indicador de loading.
 * - Botões-imagem: Register (submete) e Login (voltar ao ecrã de Login).
 */
@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,           // Callback quando o registo e criação de perfil terminam com sucesso
    onGoToLogin: () -> Unit,            // Callback para voltar ao ecrã de Login
    viewModel: RegisterViewModel = hiltViewModel() // ViewModel injetado via Hilt
) {
    // Observa o estado da UI do registo
    val uiState by viewModel.uiState

    // Contentor base
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagem de fundo do ecrã de registo
        Image(
            painter = painterResource(id = R.drawable.register),
            contentDescription = "Register Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Camada com constraints para posicionamento percentual
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val w = maxWidth // Largura total disponível
            val h = maxHeight // Altura total disponível

            // Função auxiliar para posicionar e dimensionar elementos por percentagens
            fun Modifier.boxAt(
                xPercent: Float, yPercent: Float,
                widthPercent: Float, heightPercent: Float
            ) = this
                .align(Alignment.TopStart)
                .offset(x = w * xPercent, y = h * yPercent)
                .size(width = w * widthPercent, height = h * heightPercent)

            // Campo Username
            TextField(
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChange(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                isError = uiState.usernameError != null,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.boxAt(0.12f, 0.25f, 0.76f, 0.10f)
            )
            // Erro do Username (compacto)
            uiState.usernameError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.boxAt(0.12f, 0.355f, 0.76f, 0.03f)
                )
            }

            // Campo Email
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
                modifier = Modifier.boxAt(0.12f, 0.36f, 0.76f, 0.10f)
            )
            // Erro do Email (compacto)
            uiState.emailError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.boxAt(0.12f, 0.515f, 0.76f, 0.03f)
                )
            }

            // Campo Password
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
                modifier = Modifier.boxAt(0.12f, 0.48f, 0.76f, 0.10f)
            )
            // Erro da Password (compacto)
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

            // Erro geral (pill compacta)
            uiState.generalError?.let {
                Box(
                    modifier = Modifier
                        .boxAt(0.12f, 0.62f, 0.76f, 0.04f)
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

            // Indicador de progresso enquanto o registo está a decorrer
            if (uiState.isLoading) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator()
                }
            }

            // Botão Register (submeter)
            Image(
                painter = painterResource(id = R.drawable.bbtnregister),
                contentDescription = "Register",
                modifier = Modifier
                    .boxAt(xPercent = 0.25f, yPercent = 0.70f, widthPercent = 0.5f, heightPercent = 0.09f)
                    .clickable(enabled = !uiState.isLoading) { viewModel.register(onRegistered) }
            )

            // Botão Login (voltar ao ecrã de Login)
            Image(
                painter = painterResource(id = R.drawable.bttnloginlogin),
                contentDescription = "Go to Login",
                modifier = Modifier
                    .boxAt(xPercent = 0.25f, yPercent = 0.80f, widthPercent = 0.5f, heightPercent = 0.09f)
                    .clickable(enabled = !uiState.isLoading) { onGoToLogin() }
            )
        }
    }
}
