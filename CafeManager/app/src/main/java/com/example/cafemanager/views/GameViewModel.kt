package com.example.cafemanager.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafemanager.model.GameItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.cafemanager.R
import com.example.cafemanager.model.ItemType

class GameViewModel : ViewModel() {
    // Estados do Jogo usando StateFlow (reativo para o Compose)
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _lives = MutableStateFlow(3)
    val lives: StateFlow<Int> = _lives

    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level

    private val _timer = MutableStateFlow(30)
    val timer: StateFlow<Int> = _timer

    private val _targetOrder = MutableStateFlow<List<GameItem>>(emptyList())
    val targetOrder: StateFlow<List<GameItem>> = _targetOrder

    private val _selectedItems = MutableStateFlow<List<GameItem>>(emptyList())
    val selectedItems: StateFlow<List<GameItem>> = _selectedItems

    private val _correctOrdersInLevel = MutableStateFlow(0)

    private var timerJob: Job? = null

    // Dentro da classe GameViewModel
    val allItems = listOf(
        // DRINKS
        GameItem(1, "Lungo", ItemType.DRINK, R.drawable.drink_1),
        GameItem(2, "Espresso", ItemType.DRINK, R.drawable.drink_2),
        GameItem(3, "Ristretto", ItemType.DRINK, R.drawable.drink_3),
        GameItem(4, "Macchiato", ItemType.DRINK, R.drawable.drink_4),
        GameItem(5, "Babyccino", ItemType.DRINK, R.drawable.drink_5),
        GameItem(6, "Espresso Romano", ItemType.DRINK, R.drawable.drink_6),
        GameItem(7, "Long Black", ItemType.DRINK, R.drawable.drink_7),
        GameItem(8, "Americano", ItemType.DRINK, R.drawable.drink_8),
        GameItem(9, "Flatwhite", ItemType.DRINK, R.drawable.drink_9),
        GameItem(10, "Matcha Latte", ItemType.DRINK, R.drawable.drink_10),
        GameItem(11, "Piccolo", ItemType.DRINK, R.drawable.drink_11),
        GameItem(12, "Latte", ItemType.DRINK, R.drawable.drink_12),
        GameItem(13, "Cortado", ItemType.DRINK, R.drawable.drink_13),
        GameItem(14, "Iced Black", ItemType.DRINK, R.drawable.drink_14),
        GameItem(15, "Iced Filter", ItemType.DRINK, R.drawable.drink_15),
        GameItem(16, "Negroni", ItemType.DRINK, R.drawable.drink_16),
        GameItem(17, "Iced Latte", ItemType.DRINK, R.drawable.drink_17),
        GameItem(18, "Espresso Martini", ItemType.DRINK, R.drawable.drink_18),

        // DESSERTS
        GameItem(19, "Croissant", ItemType.DESSERT, R.drawable.dessert_1),
        GameItem(20, "Cinnamonroll", ItemType.DESSERT, R.drawable.dessert_2),
        GameItem(21, "Pain au Chocolate", ItemType.DESSERT, R.drawable.dessert_3),
        GameItem(22, "Cheesecake", ItemType.DESSERT, R.drawable.dessert_4),
        GameItem(23, "Carrot Cake ", ItemType.DESSERT, R.drawable.dessert_5),
        GameItem(24, "Berry Danish", ItemType.DESSERT, R.drawable.dessert_6),
        GameItem(25, "Brownie", ItemType.DESSERT, R.drawable.dessert_7),
        GameItem(26, "Tiramisu", ItemType.DESSERT, R.drawable.dessert_8),
        GameItem(27, "Muffin", ItemType.DESSERT, R.drawable.dessert_9),
        GameItem(28, "Canele", ItemType.DESSERT, R.drawable.dessert_10),
        GameItem(29, "Chocolate Donut", ItemType.DESSERT, R.drawable.dessert_11),
        GameItem(30, "Eggtart", ItemType.DESSERT, R.drawable.dessert_12),
        GameItem(31, "Macaron", ItemType.DESSERT, R.drawable.dessert_13),
        GameItem(32, "Donut", ItemType.DESSERT, R.drawable.dessert_14)
    )

    init {
        generateNewOrder()
    }

    fun generateNewOrder() {
        // Nível 1, 2, 3 -> 2 itens
        // Nível 4, 5, 6 -> 3 itens... até o limite de 5
        val itemsToOrder = (2 + (_level.value - 1) / 3).coerceAtMost(5)

        _targetOrder.value = allItems.shuffled().take(itemsToOrder)
        clearSelection()
        resetTimer()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        _timer.value = 30
        timerJob = viewModelScope.launch {
            while (_timer.value > 0) {
                delay(1000)
                _timer.value -= 1
            }
            loseLife()
        }
    }

    fun selectItem(item: GameItem) {
        _selectedItems.value = _selectedItems.value + item
    }

    fun clearSelection() {
        _selectedItems.value = emptyList()
    }

    fun serveOrder() {
        if (_selectedItems.value == _targetOrder.value) {
            _score.value += 20
            _correctOrdersInLevel.value += 1

            // A cada 5 pedidos certos, sobe de nível
            if (_correctOrdersInLevel.value >= 5) {
                _level.value += 1
                _correctOrdersInLevel.value = 0
            }

            generateNewOrder()
        } else {
            loseLife()
        }
    }

    private fun loseLife() {
        _lives.value -= 1
        clearSelection()
        if (_lives.value > 0) generateNewOrder()
    }

    fun resetGame() {
        _score.value = 0
        _lives.value = 3
        _level.value = 1
        clearSelection()
        generateNewOrder()
    }
}