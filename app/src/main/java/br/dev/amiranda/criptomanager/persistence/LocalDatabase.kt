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
    fun getCoinIcon(symbol: String): String {
        val db = dbHelper.writableDatabase
        var svg = ""

        val cursor = db.query("icons",null,null, null,null, null, null )
        if(cursor.moveToFirst()){
            var svg = cursor.getString(cursor.getColumnIndex("svg"))
            if(svg == ""){
                svg = ResourceRequest().run() { getIconFromApi(symbol) }
                addIcon(symbol, svg)
            }
        }
        return svg
    }
}