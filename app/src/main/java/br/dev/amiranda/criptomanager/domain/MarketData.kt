package br.dev.amiranda.criptomanager.domain

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class MarketData (
    @SerializedName("market_symbol")
    val symbol: String,
    val lastTrade: Trade,

    //last 24h
    val rolling24h: Rolling24h,

    //best prices
    val ask: Trade,
    val bid: Trade

)

