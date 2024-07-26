package br.dev.amiranda.criptomanager

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.dev.amiranda.criptomanager.utils.Currency
import br.dev.amiranda.criptomanager.viewadapter.CurrencyViewAdapter
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.lang.reflect.Type
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Executors

class MainActivity : Activity() {
    private val client = OkHttpClient()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.currencyList)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // load async concurrency itens with coroutines kotlin
        CoroutineScope(Dispatchers.IO).launch {
            val currencies = getCurrenciesFromApi()
            withContext(Dispatchers.Main){
                val currencyViewAdapter = CurrencyViewAdapter(this@MainActivity, currencies)
                recyclerView.adapter = currencyViewAdapter
            }
        }
    }



    private fun getCurrenciesFromApi(): List<Currency> {
        var currencies: List<Currency>? = null

        var request = Request.Builder()
            .url("https://api.foxbit.com.br/rest/v3/currencies")
            .build()
        var response = client.newCall(request).execute()
        if (response.isSuccessful){
            val jsonString = response.body?.string()
            if (jsonString != null) {
                val gson = Gson()
                val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
                val dataJsonArray = jsonObject.getAsJsonArray("data")

                val type: Type = object : TypeToken<List<Currency>>() {}.type
                currencies = gson.fromJson(dataJsonArray, type)


            }
        }
        return currencies!!
    }
}