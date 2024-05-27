package com.andc4.terbangaja.presentation.resetpassword

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {
    private val binding : ActivityResetPasswordBinding by lazy {
        ActivityResetPasswordBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClick()
    }

    private fun setOnClick() {
        binding.ivArrowBack.setOnClickListener{
            onBackPressed()
        }
    }
}