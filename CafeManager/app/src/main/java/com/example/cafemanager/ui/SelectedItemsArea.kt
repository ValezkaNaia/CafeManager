package com.example.cafemanager.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cafemanager.model.GameItem

@Composable
fun SelectedItemsArea(
    selectedItems: List<GameItem>,
    modifier: Modifier = Modifier
) {
    // Removemos a Column externa para evitar excesso de padding,
    // já que a ActionSection já organiza o layout vertical.
    Box(
        modifier = modifier
            .fillMaxWidth(0.8f) // Ocupa 90% da largura para não encostar nas bordas
            .height(70.dp)
            // Um fundo branco semi-transparente ajuda a destacar os itens coloridos sobre o fundo
            .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (selectedItems.isEmpty()) {
            // Opcional: Um texto sutil quando nada está selecionado
        } else {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                selectedItems.forEach { item ->
                    Image(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(40.dp)
                            .offset(y = 5.dp),
                    )
                }
            }
        }
    }
}