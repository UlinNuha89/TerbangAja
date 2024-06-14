package com.andc4.terbangaja.presentation.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.databinding.ActivityPaymentBinding
import com.andc4.terbangaja.presentation.main.MainActivity

class PaymentActivity : AppCompatActivity() {
    private val binding: ActivityPaymentBinding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        doSomething()
    }

    private fun doSomething() {
        binding.payButton.setOnClickListener {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }
}
