package br.dev.amiranda.criptomanager.domain

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class MarketData (
    @SerializedName("market_symbol")
    val symbol: String,
    @SerializedName("last_trade")
    val lastTrade: Trade,
    @SerializedName("rolling_24h")
    //last 24h
    val rolling24h: Rolling24h,

    val best: Best

)

