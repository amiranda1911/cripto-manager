package br.dev.amiranda.criptomanager.domain

import java.math.BigDecimal

data class Trade(
    val price: BigDecimal,
    val volume: BigDecimal,
)
