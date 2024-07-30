package br.dev.amiranda.criptomanager.viewadapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.dev.amiranda.criptomanager.R
import br.dev.amiranda.criptomanager.utils.Currency
import com.caverock.androidsvg.SVG

class CurrencyViewAdapter(
        private val listener: OnItemClickListener,
        private var currencies: List<Currency>
    )
    : RecyclerView.Adapter<CurrencyViewAdapter.ViewHolder>() {

        interface OnItemClickListener {
            fun onItemClick(currency: Currency)
        }

        inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
            init{
                view.setOnClickListener {
                    listener.onItemClick(currencies[adapterPosition])
                }
            }
            fun bind (currency: Currency) {
                val name = itemView.findViewById<TextView>(R.id.name)
                val type = itemView.findViewById<TextView>(R.id.type)
                val icon = itemView.findViewById<ImageView>(R.id.icon)
                name.text = currency.name
                type.text = currency.type
                icon.setImageDrawable(svg2drawable(currency.icon))
            }

            fun svg2drawable (svgString: String): Drawable {
                val svg = SVG.getFromString(svgString)
                return PictureDrawable(svg.renderToPicture())
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateCurrencies(newCurrencies: List<Currency>) {
        currencies = newCurrencies
        notifyDataSetChanged() // Notifica o adaptador sobre as mudanças
    }



}