package br.dev.amiranda.criptomanager.persistence

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import br.dev.amiranda.criptomanager.ResourceRequest

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
            return svg
        }
        else{
            svg = ResourceRequest().run() { getIconFromApi(symbol) }
            addIcon(symbol, svg)
        }

        //Foxbit app limits 6 requests per second

        return svg
    }
}