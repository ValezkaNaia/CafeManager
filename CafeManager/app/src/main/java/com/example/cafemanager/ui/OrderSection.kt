package com.example.cafemanager.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafemanager.R
import com.example.cafemanager.model.GameItem

@Composable
fun OrderSection(targetOrder: List<GameItem>, timer: Int) {
    val catImageRes = remember(targetOrder) {
        val catNumber = (1..10).random()
        when (catNumber) {
            1 -> R.drawable.cat_1
            2 -> R.drawable.cat_2
            3 -> R.drawable.cat_3
            4 -> R.drawable.cat_4
            5 -> R.drawable.cat_5
            6 -> R.drawable.cat_6
            7 -> R.drawable.cat_7
            8 -> R.drawable.cat_8
            9 -> R.drawable.cat_9
            else -> R.drawable.cat_10
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fundo ocupando a tela toda
        Image(
            painter = painterResource(id = R.drawable.view_order),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Conteúdo posicionado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp, start = 40.dp, end = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = catImageRes),
                contentDescription = "Customer",
                modifier = Modifier
                    .offset(y = (-55).dp, x = (15).dp)
                    .size(60.dp)
                    .clip(CircleShape) // Sintaxe corrigida aqui
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Row(
                    modifier = Modifier.offset(y = (-57).dp, x = 5.dp)
                ) {
                    targetOrder.forEach { item ->
                        Image(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(2.dp)
                        )
                    }
                }
                Text(
                    text = "${timer}s",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 40.sp,
                    modifier = Modifier.offset(y = (-60).dp, x = 40.dp)
                )
            }
        }
    }
}