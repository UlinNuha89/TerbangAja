package com.andc4.terbangaja.presentation.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
