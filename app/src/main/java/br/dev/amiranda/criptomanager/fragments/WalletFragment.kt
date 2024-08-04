package br.dev.amiranda.criptomanager.fragments

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.dev.amiranda.criptomanager.R
import br.dev.amiranda.criptomanager.ResourceRequest
import br.dev.amiranda.criptomanager.domain.MarketData
import br.dev.amiranda.criptomanager.domain.Wallet
import br.dev.amiranda.criptomanager.persistence.LocalDatabase
import br.dev.amiranda.criptomanager.utils.Currency
import com.caverock.androidsvg.SVG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal


class WalletFragment : Fragment() {
    private lateinit var currency: Currency
    private lateinit var marketData: MarketData
    private lateinit var wallets: List<Wallet>

    private lateinit var coinIcon: ImageView
    private lateinit var walletsValue: TextView
    private lateinit var cotation: TextView

    private var totalWalletsValue = BigDecimal.valueOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currency = Currency(
            arguments?.getString("name")!!,
            arguments?.getString("symbol")!!,
            arguments?.getString("type")!!,
            arguments?.getString("icon")!!
        )
        loadMarketData()

        val view = inflater.inflate(R.layout.fragment_wallet, container, false)

        coinIcon = view.findViewById(R.id.coinIcon)
        walletsValue = view.findViewById(R.id.totalValue)
        cotation = view.findViewById(R.id.cotation)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        wallets = LocalDatabase(requireContext()).run { getWallets(currency.symbol) }
        wallets.stream().forEach {wallet -> totalWalletsValue += wallet.value}


        val svg = SVG.getFromString(currency.icon)
        coinIcon.setImageDrawable(PictureDrawable(svg.renderToPicture()))
    }

    private fun loadMarketData() {
        CoroutineScope(Dispatchers.IO).launch {
            val market = ResourceRequest().run { getMarketTicker(currency.symbol) }

            withContext(Dispatchers.Main) {
                marketData = market
                updateWalletData()
            }
        }
    }

    private fun updateWalletData() {
        var totalBuyValue = BigDecimal.valueOf(0)
        var totalSellValue = BigDecimal.valueOf(0)

        wallets.stream().forEach{
            wallet -> totalBuyValue += (wallet.value * wallet.buyValue)
        }
        wallets.stream().forEach {
            wallet -> totalSellValue += (wallet.value * marketData.lastTrade.price)
        }

        var actualCotation = BigDecimal.ZERO
        if(!(totalBuyValue.equals(BigDecimal.ZERO) || totalSellValue.equals(BigDecimal.ZERO))){
            actualCotation = ((totalBuyValue - totalSellValue) / totalSellValue) * BigDecimal.valueOf(100)
        }

        walletsValue.text = "R$ ${totalWalletsValue}"
        cotation.text = "${actualCotation}%"


    }
}