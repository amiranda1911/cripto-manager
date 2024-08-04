package br.dev.amiranda.criptomanager.persistence

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "storage", null, 1) {

    private val createIconsTable = "CREATE TABLE icons " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "symbol TEXT NOT NULL, " +
            "svg TEXT )"

    private val createWalletTable = "CREATE TABLE wallets" +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "symbol TEXT NOT NULL," +
            "value REAL NOT NULL," +
            "buy_value REAL NOT NULL," +
            "sell_value REAL)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createIconsTable)
        db.execSQL(createWalletTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS icons")
        db.execSQL("DROP TABLE IF EXISTS wallets")
        onCreate(db)
    }
    
}