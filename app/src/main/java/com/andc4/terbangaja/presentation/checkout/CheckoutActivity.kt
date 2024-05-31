package com.andc4.terbangaja.presentation.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {
    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
