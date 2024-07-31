package br.dev.amiranda.criptomanager

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun getMarket_isCorrect(){
        assertNotNull(ResourceRequest().run { getMarketTicker("btc") })
    }
}