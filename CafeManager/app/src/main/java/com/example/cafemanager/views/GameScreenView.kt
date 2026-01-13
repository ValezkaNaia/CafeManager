package com.example.cafemanager.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cafemanager.R
import com.example.cafemanager.model.GameItem
import com.example.cafemanager.model.ItemType
import com.example.cafemanager.navigation.Routes
import com.example.cafemanager.ui.OrderSection
import com.example.cafemanager.ui.SelectedItemsArea
import com.example.cafemanager.ui.SelectionDialog
import com.example.cafemanager.ui.StatusRow

/**
 * Ecrã do Jogo
 * - Integra a lógica do GameViewModel com UI em Compose.
 * - Usa diálogos de seleção de bebidas e sobremesas, área de itens selecionados e botões de ação.
 * - Quando as vidas chegam a 0, navega para o ecrã de Game Over.
 */
@Composable
fun GameScreen(
    navController: NavController,
    gameViewModel: GameViewModel = viewModel()
) {
    val score by gameViewModel.score.collectAsState()
    val lives by gameViewModel.lives.collectAsState()
    val timer by gameViewModel.timer.collectAsState()
    val level by gameViewModel.level.collectAsState()
    val targetOrder by gameViewModel.targetOrder.collectAsState()
    val selectedItems by gameViewModel.selectedItems.collectAsState()

    var showDrinksDialog by remember { mutableStateOf(false) }
    var showDessertsDialog by remember { mutableStateOf(false) }

    val drinks = remember { gameViewModel.allItems.filter { it.type == ItemType.DRINK } }
    val desserts = remember { gameViewModel.allItems.filter { it.type == ItemType.DESSERT } }

    // Audio
    val context = androidx.compose.ui.platform.LocalContext.current
    val gameMusic = com.example.cafemanager.utils.rememberLoopingMusic(resId = R.raw.gamemusic)
    LaunchedEffect(Unit) {
        gameMusic.start()
    }

    // Efeito sonoro quando o nível aumenta
    var prevLevel by remember { mutableStateOf(level) }
    LaunchedEffect(level) {
        if (level > prevLevel) {
            com.example.cafemanager.utils.playSfx(context, R.raw.passlevel)
        }
        prevLevel = level
    }

    // Quando as vidas chegarem a 0, guardar score e ir para Game Over
    val gameOverViewModel: GameOverViewModel = androidx.hilt.navigation.compose.hiltViewModel()
    LaunchedEffect(lives) {
        if (lives <= 0) {
            // Guardar score e atualizar stats no Firestore (se autenticado)
            gameOverViewModel.saveScoreIfLoggedIn(score, level)
            navController.navigate(Routes.GAME_OVER_SCREEN) {
                popUpTo(Routes.GAME_SCREEN) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fundo e pedido atual + timer
        OrderSection(targetOrder = targetOrder, timer = timer)

        // UI por cima
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            StatusRow(lives = lives, score = score, level = level)

            Spacer(modifier = Modifier.weight(1f))

            ActionSection(
                onClear = {
                    com.example.cafemanager.utils.playSfx(context, R.raw.buttonclick)
                    gameViewModel.clearSelection()
                },
                onServe = {
                    com.example.cafemanager.utils.playSfx(context, R.raw.buttonclick)
                    gameViewModel.serveOrder()
                },
                onOpenDrinks = {
                    com.example.cafemanager.utils.playSfx(context, R.raw.buttonclick)
                    showDrinksDialog = true
                },
                onOpenDesserts = {
                    com.example.cafemanager.utils.playSfx(context, R.raw.buttonclick)
                    showDessertsDialog = true
                },
                selectedItems = selectedItems
            )
        }
    }

    // Diálogo de bebidas
    if (showDrinksDialog) {
        SelectionDialog(
            titleImageRes = R.drawable.view_drinks,
            items = drinks,
            onItemClick = {
                com.example.cafemanager.utils.playSfx(context, R.raw.buttonclick)
                gameViewModel.selectItem(it)
            },
            onDismiss = { showDrinksDialog = false }
        )
    }
    // Diálogo de sobremesas
    if (showDessertsDialog) {
        SelectionDialog(
            titleImageRes = R.drawable.view_desserts,
            items = desserts,
            onItemClick = {
                com.example.cafemanager.utils.playSfx(context, R.raw.buttonclick)
                gameViewModel.selectItem(it)
            },
            onDismiss = { showDessertsDialog = false }
        )
    }
}

@Composable
private fun ActionSection(
    onClear: () -> Unit,
    onServe: () -> Unit,
    onOpenDrinks: () -> Unit,
    onOpenDesserts: () -> Unit,
    selectedItems: List<GameItem>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botões verticais de menu
        Column(
            modifier = Modifier.height(250.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.button_drink),
                contentDescription = "Drinks",
                modifier = Modifier
                    .size(200.dp, 110.dp)
                    .offset(y = 125.dp)
                    .clickable { onOpenDrinks() }
            )
            Image(
                painter = painterResource(id = R.drawable.button_dessert),
                contentDescription = "Desserts",
                modifier = Modifier
                    .size(200.dp, 110.dp)
                    .offset(y = 100.dp)
                    .clickable { onOpenDesserts() }
            )
        }

        // Área de itens selecionados
        SelectedItemsArea(
            selectedItems = selectedItems,
            modifier = Modifier.offset(y = 100.dp)
        )

        // Botões de ação lado a lado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.button_clear),
                contentDescription = "Clear",
                modifier = Modifier
                    .size(120.dp)
                    .offset(y = 46.dp, x = 15.dp)
                    .clickable { onClear() }
            )
            Image(
                painter = painterResource(id = R.drawable.button_serve),
                contentDescription = "Serve",
                modifier = Modifier
                    .size(120.dp)
                    .offset(y = 46.dp, x = (-10).dp)
                    .clickable { onServe() }
            )
        }
    }
}
