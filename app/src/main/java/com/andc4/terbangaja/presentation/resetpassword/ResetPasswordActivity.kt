package com.andc4.terbangaja.presentation.resetpassword

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.ActivityResetPasswordBinding
import com.andc4.terbangaja.databinding.LayoutDialogBinding
import com.andc4.terbangaja.utils.proceed
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordActivity : AppCompatActivity() {
    private val binding: ActivityResetPasswordBinding by lazy {
        ActivityResetPasswordBinding.inflate(layoutInflater)
    }
    private val viewModel: ResetPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClick()
    }

    private fun setOnClick() {
        binding.ivArrowBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSend.setOnClickListener {
            doSendEmail()
        }
    }

    private fun doSendEmail() {
        if (isFormValid()) {
            val email = binding.etEmail.text.toString().trim()
            proceedSendEmail(email)
        }
    }

    private fun proceedSendEmail(email: String) {
        viewModel.sendEmail(email).observe(this) { it ->
            it.proceed(
                doOnSuccess = {
                    binding.cvLoading.isVisible = false
                    binding.btnSend.isVisible = true
                    showDialog()
                },
                doOnLoading = {
                    binding.cvLoading.isVisible = true
                    binding.btnSend.isVisible = false
                },
                doOnError = {
                    binding.cvLoading.isVisible = false
                    binding.btnSend.isVisible = true
                    Toast.makeText(this, "Gagal kirim", Toast.LENGTH_SHORT).show()
                    Log.e("GagalReset", it.exception.toString())
                },
            )
        }
    }

    private fun isFormValid(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        return validateEmail(email)
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

    private fun showDialog() {
        val dialogBinding = LayoutDialogBinding.inflate(LayoutInflater.from(this))
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogBinding.root)
        val dialog = alertDialogBuilder.create()
        dialogBinding.btnOk.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        dialog.show()
    }
}
