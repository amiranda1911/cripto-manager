package br.dev.amiranda.criptomanager.persistence

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "storage", null, 1) {

    private val createSQL = "CREATE TABLE icons " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "symbol TEXT NOT NULL, " +
            "svg TEXT )"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS icons")
        onCreate(db)
    }
    
}