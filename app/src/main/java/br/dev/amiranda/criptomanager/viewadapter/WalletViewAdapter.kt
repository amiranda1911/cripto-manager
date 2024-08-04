package br.dev.amiranda.criptomanager.viewadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.dev.amiranda.criptomanager.R
import br.dev.amiranda.criptomanager.domain.MarketData
import br.dev.amiranda.criptomanager.domain.Wallet
import java.math.BigDecimal

class WalletViewAdapter(
    private val listener: CurrencyViewAdapter.OnItemClickListener,
    private val wallets: List<Wallet>,
    private val marketData: MarketData
    ) : RecyclerView.Adapter<WalletViewAdapter.ViewHolder>() {

        inner class ViewHolder(view : View) :RecyclerView.ViewHolder(view) {
            @SuppressLint("SetTextI18n")
            fun bind (wallet: Wallet) {
                val quantity = itemView.findViewById<TextView>(R.id.quantity)
                val price = itemView.findViewById<TextView>(R.id.price)
                val walletValue = itemView.findViewById<TextView>(R.id.wallet_value)
                val cotation = itemView.findViewById<TextView>(R.id.cotation)

                quantity.text = wallet.value.toString()
                price.text = marketData.lastTrade.price.toString()
                walletValue.text = (marketData.lastTrade.price *
                                    wallet.value).toString()
                val buy = wallet.buyValue * wallet.value
                val actual = marketData.lastTrade.price * wallet.value
                val diff = buy - actual

                val percent = (diff/actual) * BigDecimal.valueOf(100)

                cotation.text = percent.toString()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wallet_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wallets.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wallet = wallets[position]
        holder.bind(wallet)
    }
}