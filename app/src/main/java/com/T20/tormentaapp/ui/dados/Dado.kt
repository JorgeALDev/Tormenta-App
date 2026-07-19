package com.T20.tormentaapp.ui.dados

import androidx.annotation.DrawableRes

data class Dado (
    val nome: String,
    val lados: Int,
    @DrawableRes val icone: Int
)