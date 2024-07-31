package br.dev.amiranda.criptomanager.domain

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.Date

data class Trade(
    val price: BigDecimal,
    val volume: BigDecimal,
    val date: Date
)
