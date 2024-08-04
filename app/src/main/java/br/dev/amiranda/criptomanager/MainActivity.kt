package br.dev.amiranda.criptomanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import br.dev.amiranda.criptomanager.fragments.CurrenciesFragment

class MainActivity : FragmentActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, CurrenciesFragment())
                .commit()
        }
    }
}