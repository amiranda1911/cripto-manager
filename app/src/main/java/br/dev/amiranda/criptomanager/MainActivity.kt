package br.dev.amiranda.criptomanager

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.dev.amiranda.criptomanager.persistence.LocalDatabase
import br.dev.amiranda.criptomanager.utils.Currency
import br.dev.amiranda.criptomanager.viewadapter.CurrencyViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

class MainActivity : Activity() {
    private val client = OkHttpClient()
    private val database = LocalDatabase(this)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.currencyList)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // load async concurrency itens with coroutines kotlin
        CoroutineScope(Dispatchers.IO).launch {
            val currencies = ResourceRequest().run(){ getCurrenciesFromApi() }
            val currenciesWithIcons: List<Currency> = currencies.map {
                    currency ->
                var icon = database.getCoinIcon(currency.symbol)
                currency.copy(icon = icon)
            }
            withContext(Dispatchers.Main){
                val currencyViewAdapter = CurrencyViewAdapter(this@MainActivity, currenciesWithIcons)
                recyclerView.adapter = currencyViewAdapter
            }
        }
    }




}