package br.dev.amiranda.criptomanager.domain

import java.math.BigDecimal

data class Wallet(
    val symbol: String,
    val value: BigDecimal,
    val buyValue: BigDecimal,
    var sellValue: BigDecimal
)
