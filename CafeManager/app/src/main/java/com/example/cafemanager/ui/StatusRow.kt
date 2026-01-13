package com.example.cafemanager.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafemanager.R

@Composable
fun StatusRow(lives: Int, score: Int, level: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Exibe corações
        Row {
            repeat(3) { index ->
                Image(
                    painter = painterResource(id = R.drawable.heart),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(y = (210).dp, x = (35).dp)
                        .size(30.dp),
                    // Se a vida for menor que o index, podemos deixar o coração transparente
                    alpha = if (index < lives) 1f else 0.3f
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "$score",
                fontSize = 30.sp,
                modifier = Modifier.offset(y = 230.dp, x = (-177).dp))
            Text(
                "$level",
                fontSize = 30.sp,
                modifier = Modifier.offset(y = 200.dp, x = 23.dp))
        }
    }
}