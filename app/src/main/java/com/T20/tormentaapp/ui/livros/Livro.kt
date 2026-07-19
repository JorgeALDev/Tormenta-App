package com.T20.tormentaapp.ui.livros

import androidx.annotation.DrawableRes

data class Livro(
    val titulo: String,
    @DrawableRes val capa: Int,
    val url: String
)