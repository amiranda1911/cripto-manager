package br.dev.amiranda.criptomanager.fragments

import android.graphics.Color
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.dev.amiranda.criptomanager.R
import br.dev.amiranda.criptomanager.ResourceRequest
import br.dev.amiranda.criptomanager.persistence.LocalDatabase
import br.dev.amiranda.criptomanager.utils.Currency
import com.caverock.androidsvg.SVG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class MarketFragment : Fragment() {
    private lateinit var database: LocalDatabase

    private lateinit var currency: Currency
    private lateinit var coinIcon: ImageView
    private lateinit var name: TextView
    private lateinit var price: TextView
    private lateinit var symbol: TextView
    private lateinit var priceChangePercent: TextView
    private lateinit var walletButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_market, container, false)

        database = LocalDatabase(requireContext())

        coinIcon = view.findViewById<ImageView>(R.id.coinIcon)
        name = view.findViewById<TextView>(R.id.name)
        price = view.findViewById<TextView>(R.id.price)
        symbol = view.findViewById<TextView>(R.id.symbol)
        priceChangePercent = view.findViewById<TextView>(R.id.price_change_percent)
        walletButton = view.findViewById<Button>(R.id.wallet_button)

        return view
    }

    fun onWalletClick(){
        val fragment = WalletFragment()
        val bundle = Bundle()

        bundle.putString("name", currency.name)
        bundle.putString("symbol", currency.symbol)
        bundle.putString("type", currency.type)
        bundle.putString("icon", currency.icon)

        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currency = Currency(
            arguments?.getString("name")!!,
            arguments?.getString("symbol")!!,
            arguments?.getString("type")!!,
            arguments?.getString("icon")!!
        )

        name.text = currency.name
        symbol.text = currency.symbol
        loadCoinIcon()
        loadMarketData()

        walletButton.setOnClickListener{
            onWalletClick()
        }
    }

    private fun loadCoinIcon() {
        CoroutineScope(Dispatchers.IO).launch {
            val icon = database.getCoinIcon(currency.symbol)
            withContext(Dispatchers.Main){
                currency.icon = icon
                val svg = SVG.getFromString(currency.icon)
                coinIcon.setImageDrawable(PictureDrawable(svg.renderToPicture()))
            }
        }
    }
    private fun loadMarketData() {
        CoroutineScope(Dispatchers.IO).launch {
            val market = ResourceRequest().run { getMarketTicker(currency.symbol) }

            withContext(Dispatchers.Main){
                val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                val priceFormatted = formatter.format(market.lastTrade.price)
                price.text = priceFormatted
                formatter.minimumFractionDigits = 2
                formatter.maximumFractionDigits = 2
                val percent  = formatter.format(market.rolling24h.priceChangePercent)
                priceChangePercent.text = "${percent}%"
                if(market.rolling24h.priceChangePercent.toDouble() > 0) {
                    priceChangePercent.setTextColor(Color.GREEN)
                }else {
                    priceChangePercent.setTextColor(Color.RED)
                }
            }
        }
    }
}