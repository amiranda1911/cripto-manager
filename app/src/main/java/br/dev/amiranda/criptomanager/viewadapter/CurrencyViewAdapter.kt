package br.dev.amiranda.criptomanager.viewadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.dev.amiranda.criptomanager.R
import br.dev.amiranda.criptomanager.utils.Currency

class CurrencyViewAdapter(
        private val context : Context,
        private val currencies: List<Currency>
    )
    : RecyclerView.Adapter<CurrencyViewAdapter.ViewHolder>() {

        class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
            fun bind (currency: Currency) {
                val name = itemView.findViewById<TextView>(R.id.name)
                val type = itemView.findViewById<TextView>(R.id.type)

                name.text = currency.name
                type.text = currency.type
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate not attached to root
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency)
    }
}