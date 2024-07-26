package br.dev.amiranda.criptomanager.utils

data class Currency (
    val symbol: String,
    val icon: String,
    val name: String,
    val type: String,
    val category : Category?
)