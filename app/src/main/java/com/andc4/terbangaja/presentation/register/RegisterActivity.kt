package com.andc4.terbangaja.presentation.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.databinding.ActivityRegisterBinding
import com.andc4.terbangaja.presentation.login.LoginActivity
import com.andc4.terbangaja.presentation.otp.OtpActivity

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListener()
    }

    private fun setClickListener() {
        binding.ivArrow.setOnClickListener {
            onBackPressed()
        }
        binding.btnDaftar.setOnClickListener {
            startActivity(Intent(this, OtpActivity::class.java))
        }
        binding.tvNavToLogin.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                },
            )
        }
    }
}
