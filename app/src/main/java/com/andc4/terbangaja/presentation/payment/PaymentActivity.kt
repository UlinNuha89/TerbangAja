package com.andc4.terbangaja.presentation.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    private val binding: ActivityPaymentBinding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
