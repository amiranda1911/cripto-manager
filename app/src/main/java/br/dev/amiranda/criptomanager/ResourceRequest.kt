package br.dev.amiranda.criptomanager

import br.dev.amiranda.criptomanager.domain.MarketData
import br.dev.amiranda.criptomanager.utils.Currency
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type

class ResourceRequest {
    private val client = OkHttpClient()

    suspend fun getCurrenciesFromApi(): List<Currency> {
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

    suspend fun getIconFromApi(symbol : String) : String {
        var request = Request.Builder()
            .url("https://statics.foxbit.com.br/icons/colored/${symbol}.svg")
            .build()
        var response = client.newCall(request).execute()

        if (response.isSuccessful){
            val svg = response.body?.string()
            return svg!!
        }
        return return "\n" +
                "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
                "<!-- Uploaded to: SVG Repo, www.svgrepo.com, Generator: SVG Repo Mixer Tools -->\n" +
                "<svg height=\"800px\" width=\"800px\" version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n" +
                "\t viewBox=\"0 0 291.764 291.764\" xml:space=\"preserve\">\n" +
                "<g>\n" +
                "\t<path style=\"fill:#EFC75E;\" d=\"M145.882,0c80.573,0,145.882,65.319,145.882,145.882s-65.31,145.882-145.882,145.882\n" +
                "\t\tS0,226.446,0,145.882S65.31,0,145.882,0z\"/>\n" +
                "\t<path style=\"fill:#CC9933;\" d=\"M145.882,27.353c-65.465,0-118.529,53.065-118.529,118.529s53.065,118.529,118.529,118.529\n" +
                "\t\ts118.529-53.065,118.529-118.529S211.347,27.353,145.882,27.353z M145.882,246.176c-55.39,0-100.294-44.904-100.294-100.294\n" +
                "\t\tS90.493,45.588,145.882,45.588s100.294,44.904,100.294,100.294S201.281,246.176,145.882,246.176z M158.009,138.269\n" +
                "\t\tc-5.452-2.289-25.493-5.452-25.493-14.214c0-6.464,7.495-8.334,11.99-8.334c4.094,0,8.999,1.295,11.589,3.875\n" +
                "\t\tc1.641,1.577,2.316,2.726,2.854,4.313c0.684,1.869,1.094,3.875,3.684,3.875h13.357c3.136,0,3.957-0.574,3.957-4.021\n" +
                "\t\tc0-14.789-11.589-23.122-24.809-25.994V86.28c0-2.58-0.821-4.167-3.957-4.167h-10.64c-3.136,0-3.957,1.577-3.957,4.167v11.051\n" +
                "\t\tc-14.178,2.726-26.031,11.634-26.031,27.718c0,18.235,12.683,23.979,26.441,28.566c11.589,3.884,23.724,4.021,23.724,12.063\n" +
                "\t\ts-5.99,9.765-13.357,9.765c-5.051,0-10.631-1.304-13.366-4.741c-1.769-2.152-2.453-4.021-2.58-5.89\n" +
                "\t\tc-0.274-3.592-1.769-4.021-4.914-4.021H113.28c-3.136,0-3.957,0.729-3.957,4.021c0,16.366,13.093,26.286,27.262,29.441v11.206\n" +
                "\t\tc0,2.58,0.821,4.167,3.957,4.167h10.64c3.136,0,3.957-1.586,3.957-4.167v-10.905c16.084-2.453,27.125-12.209,27.125-29.441\n" +
                "\t\tC182.28,145.591,167.829,141.424,158.009,138.269z\"/>\n" +
                "</g>\n" +
                "</svg>"
    }

    suspend fun getMarketTicker(symbol: String ): MarketData{
        var marketData: MarketData? = null
        var jsonString = requestResource("https://api.foxbit.com.br/rest/v3/markets/${symbol}brl/ticker/24hr")
        if (jsonString != null){
            val gson = Gson()
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
            val dataJsonArray = jsonObject.getAsJsonArray("data")

            val type: Type = object : TypeToken<MarketData>() {}.type
            marketData = gson.fromJson(dataJsonArray[0], type)
        }
        return marketData!!
    }


    private suspend fun requestResource(url: String) : String?{
        var request = Request.Builder()
            .url(url)
            .build()
        var response = client.newCall(request).execute()

        if (response.isSuccessful)
            return response.body?.string()
        return null!!
    }

}