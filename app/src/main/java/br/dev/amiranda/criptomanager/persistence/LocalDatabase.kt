package br.dev.amiranda.criptomanager.persistence

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import br.dev.amiranda.criptomanager.ResourceRequest
import br.dev.amiranda.criptomanager.domain.Wallet
import java.math.BigDecimal

class LocalDatabase(context: Context){

    private val dbHelper = LocalDatabaseHelper(context)


    fun addIcon(symbol: String, svg: String) {
        val db = dbHelper.writableDatabase
        val coinIcon = ContentValues().apply{
            put("symbol", symbol)
            put("svg", svg)
        }
        db.insert("icons", null, coinIcon)
        db.close()
    }

    @SuppressLint("Range")
    suspend fun getCoinIcon(symbol: String): String {
        val db = dbHelper.writableDatabase
        var svg: String

        val cursor = db.query("icons",null,"symbol = \"$symbol\"", null,null, null, null )
        if(cursor.moveToFirst()){
            svg = cursor.getString(cursor.getColumnIndex("svg"))
            cursor.close()
            return svg
        }
        else{
            svg = ResourceRequest().run() { getIconFromApi(symbol) }
            addIcon(symbol, svg)
        }
        //Foxbit app limits 6 requests per second
        return svg
    }

    fun addWallet(wallet: Wallet) {
        val db = dbHelper.writableDatabase
        val newWallet = ContentValues().apply{
            put("symbol", wallet.symbol)
            put("value", wallet.value.toDouble())
            put("buy_value", wallet.buyValue.toDouble())
        }
        db.insert("wallets", null, newWallet)
        db.close()
    }

    @SuppressLint("Range")
    fun getWallets(symbol: String) : List<Wallet> {
        val db = dbHelper.writableDatabase
        var wallets: List<Wallet> = listOf()

        val cursor = db.query("wallets",null,"symbol = \"$symbol\"", null,null, null, null )
        cursor.use{
            while(cursor.moveToNext()) {
                var wallet = Wallet(
                    cursor.getString(cursor.getColumnIndex("symbol")),
                    BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("value"))),
                    BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("buy_value"))),
                    BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("sell_value")))
                    )
                wallets = wallets + wallet
            }

        }
        return wallets
    }

}