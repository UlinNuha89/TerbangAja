package com.andc4.terbangaja.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.ActivityLoginBinding
import com.andc4.terbangaja.presentation.main.MainActivity
import com.andc4.terbangaja.presentation.register.RegisterActivity
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListener()
    }

    private fun setClickListener() {
        binding.tvNavToRegister.setOnClickListener {
            navigateToRegister()
        }
        binding.btnLogin.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        if (isFormValid()) {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupForm() {
        binding.tilPassword.isVisible = true
        binding.tilEmail.isVisible = true
    }

    private fun isFormValid(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        return validateEmail(email) && validatePassword(password, binding.tilPassword)
    }

    private fun validateEmail(email: String): Boolean {
        val errorMsg =
            when {
                email.isEmpty() -> getString(R.string.text_error_email_empty)
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> getString(R.string.text_error_email_invalid)
                else -> null
            }
        binding.tilEmail.isErrorEnabled = errorMsg != null
        binding.tilEmail.error = errorMsg
        return errorMsg == null
    }

    private fun validatePassword(
        password: String,
        textInputLayout: TextInputLayout,
    ): Boolean {
        val errorMsg =
            when {
                password.isEmpty() -> getString(R.string.text_error_password_empty)
                password.length < 8 -> getString(R.string.text_error_password_less_than_8_char)
                else -> null
            }
        textInputLayout.isErrorEnabled = errorMsg != null
        textInputLayout.error = errorMsg
        return errorMsg == null
    }

    private fun navigateToMain() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
