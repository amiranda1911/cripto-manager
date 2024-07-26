package br.dev.amiranda.criptomanager

import br.dev.amiranda.criptomanager.utils.Currency
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type

class ResourceRequest {
    private val client = OkHttpClient()

    fun getCurrenciesFromApi(): List<Currency> {
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