package com.example.cafemanager.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cafemanager.R
import com.example.cafemanager.model.GameItem


@Composable
fun SelectionDialog(
    titleImageRes: Int,
    items: List<GameItem>,
    onItemClick: (GameItem) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f) // Quase a largura toda da tela
                .fillMaxHeight(0.7f) // 70% da altura da tela
        ) {
            Image(
                painter = painterResource(id = titleImageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth() // Faz a imagem ocupar a largura total disponível
                    .size(2000.dp), // Aumenta aqui a altura conforme gostares
                contentScale = ContentScale.Fit // Mantém a proporção sem cortar
            )

            // Botão Close (X) - Fazer maior para ser fácil clicar
            Image(
                painter = painterResource(id = R.drawable.button_close),
                contentDescription = "Close",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(45.dp)
                    .clickable { onDismiss() }
            )

            // Grid de itens
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp, start = 50.dp, end = 50.dp, bottom = 20.dp),
                // ESPAÇO ENTRE AS LINHAS (vertical)
                verticalArrangement = Arrangement.spacedBy(5.dp),
                // ESPAÇO ENTRE AS COLUNAS (horizontal)
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items) { item ->
                    ItemIcon(item = item, onClick = { onItemClick(item) })
                }
            }
        }
    }
}


@Composable
fun ItemIcon(item: GameItem, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.name,
            modifier = Modifier.size(48.dp)
        )

    }
}

