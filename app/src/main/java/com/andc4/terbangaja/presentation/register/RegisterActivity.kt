package com.andc4.terbangaja.presentation.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.ActivityRegisterBinding
import com.andc4.terbangaja.presentation.login.LoginActivity
import com.andc4.terbangaja.presentation.otp.OtpActivity
import com.andc4.terbangaja.utils.proceedWhen
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
    }

    private fun setupForm() {
        binding.tilConfirmPassword.isVisible = true
        binding.tilTelp.isVisible = true
        binding.tilName.isVisible = true
        binding.tilPassword.isVisible = true
        binding.tilEmail.isVisible = true
        setClickListener()
    }

    private fun setClickListener() {
        binding.ivArrow.setOnClickListener {
            onBackPressed()
        }
        binding.btnDaftar.setOnClickListener {
            doRegistration()
        }
        binding.tvNavToLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun doRegistration() {
        if (isFormValid()) {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val telp = binding.etTelp.text.toString().trim()
            // Toast.makeText(this, "Regist $email, $password, $name, $telp", Toast.LENGTH_SHORT).show()
            proceedRegister(name, email, password, telp)
        }
    }

    private fun proceedRegister(
        name: String,
        email: String,
        password: String,
        telp: String,
    ) {
        viewModel.doRegister(name, email, telp, password).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let {
                        if (it.isVerified == false) {
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                            viewModel.setToken(it.token)
                            navigateToOTP(email)
                        } else {
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                doOnError = {
                    binding.btnDaftar.text = it.exception?.message
                },
            )
        }
    }

    private fun isFormValid(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confPassword = binding.etConfirmPassword.text.toString().trim()
        val name = binding.etName.text.toString().trim()
        val telp = binding.etTelp.text.toString().trim()

        return validateAllFieldsFilled(email, password, confPassword, name, telp) &&
            validateEmail(email) &&
            validatePassword(password, binding.tilPassword) &&
            validatePassword(confPassword, binding.tilConfirmPassword) &&
            passwordsMatch(password, confPassword) &&
            validatePhoneNumber(telp)
    }

    private fun validateAllFieldsFilled(
        email: String,
        password: String,
        confPassword: String,
        name: String,
        telp: String,
    ): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = getString(R.string.text_error_email_empty)
            isValid = false
        } else {
            binding.tilEmail.isErrorEnabled = false
        }

        if (password.isEmpty()) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = getString(R.string.text_error_password_empty)
            isValid = false
        } else {
            binding.tilPassword.isErrorEnabled = false
        }

        if (confPassword.isEmpty()) {
            binding.tilConfirmPassword.isErrorEnabled = true
            binding.tilConfirmPassword.error = getString(R.string.text_error_password_empty)
            isValid = false
        } else {
            binding.tilConfirmPassword.isErrorEnabled = false
        }

        if (name.isEmpty()) {
            binding.tilName.isErrorEnabled = true
            binding.tilName.error = getString(R.string.text_error_name_empty)
            isValid = false
        } else {
            binding.tilName.isErrorEnabled = false
        }

        if (telp.isEmpty()) {
            binding.tilTelp.isErrorEnabled = true
            binding.tilTelp.error = getString(R.string.text_error_telp_empty)
            isValid = false
        } else {
            binding.tilTelp.isErrorEnabled = false
        }
        return isValid
    }

    private fun passwordsMatch(
        password: String,
        confirmPassword: String,
    ): Boolean {
        return if (password != confirmPassword) {
            showPasswordMismatchError(true)
            false
        } else {
            showPasswordMismatchError(false)
            true
        }
    }

    private fun showPasswordMismatchError(isError: Boolean) {
        binding.tilConfirmPassword.isErrorEnabled = isError
        if (isError) {
            val errorMsg = getString(R.string.text_password_does_not_match)
            binding.tilConfirmPassword.error = errorMsg
        }
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

    private fun validatePhoneNumber(telp: String): Boolean {
        val errorMsg =
            when {
                telp.isEmpty() -> "Nomor telepon tidak boleh kosong"
                telp.length < 11 -> "Nomor telepon tidak boleh kurang dari 11 digit"
                telp.length > 13 -> "Nomor telepon tidak boleh lebih dari 13 digit"
                else -> null
            }
        binding.tilTelp.isErrorEnabled = errorMsg != null
        binding.tilTelp.error = errorMsg
        return errorMsg == null
    }

    private fun validateEmail(email: String): Boolean {
        val errorMsg =
            when {
                email.isEmpty() -> getString(R.string.text_error_email_empty)
                !Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches() -> getString(R.string.text_error_email_invalid)

                else -> null
            }
        binding.tilEmail.isErrorEnabled = errorMsg != null
        binding.tilEmail.error = errorMsg
        return errorMsg == null
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }

    private fun navigateToOTP(email: String) {
        startActivity(
            Intent(this, OtpActivity::class.java).apply { putExtra("email", email) },
        )
    }
}
