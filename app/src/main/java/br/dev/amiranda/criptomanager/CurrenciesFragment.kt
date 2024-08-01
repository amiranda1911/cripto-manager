package br.dev.amiranda.criptomanager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

class CurrenciesFragment : Fragment(), CurrencyViewAdapter.OnItemClickListener {

    private lateinit var database: LocalDatabase
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // load async concurrency itens with coroutines kotlin

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_currencies, container, false)

        database = LocalDatabase(requireContext())

        val currencyViewAdapter = CurrencyViewAdapter(this, emptyList())

        recyclerView = view.findViewById(R.id.currencyList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = currencyViewAdapter

        loadCurrencies()

        return view
    }

    private fun loadCurrencies() {
        CoroutineScope(Dispatchers.IO).launch {
            val currencies = ResourceRequest().run(){ getCurrenciesFromApi() }
            val currenciesWithIcons: List<Currency> = currencies.map {
                    currency ->
                var icon = database.getCoinIcon(currency.symbol)
                currency.copy(icon = icon)
            }
            withContext(Dispatchers.Main){
                if (isAdded) {
                    (recyclerView.adapter as CurrencyViewAdapter).updateCurrencies(currenciesWithIcons)
                }
            }
        }
    }

    override fun onItemClick(currency: Currency) {
        val fragment = MarketFragment()
        val bundle = Bundle()

        bundle.putString("name", currency.name)
        bundle.putString("symbol", currency.symbol)
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null) // Adicionar a transação à pilha de backstack
            .commit()
    }
}