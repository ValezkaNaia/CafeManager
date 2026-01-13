package com.example.cafemanager.model

import androidx.annotation.DrawableRes

data class GameItem(
    val id: Int,
    val name: String,
    val type: ItemType,
    @DrawableRes val iconRes: Int
)

enum class ItemType { DRINK, DESSERT }