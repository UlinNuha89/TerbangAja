package com.andc4.terbangaja.presentation.checkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.databinding.ActivityCheckoutBinding
import com.andc4.terbangaja.presentation.payment.PaymentActivity

class CheckoutActivity : AppCompatActivity() {
    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClick()
    }

    private fun setOnClick() {
        binding.tvPaymentButton.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }
    }
}
