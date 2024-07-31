package br.dev.amiranda.criptomanager.domain

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Rolling24h(
    @SerializedName("price_change")
    val priceChange: BigDecimal,
    @SerializedName("price_change_percent")
    val priceChangePercent: BigDecimal,
    val volume: BigDecimal,
    @SerializedName("trades_count")
    val tradesCount: Int,
    val open: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal
)
