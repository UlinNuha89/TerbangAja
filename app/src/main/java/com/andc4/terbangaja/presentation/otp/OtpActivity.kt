package com.andc4.terbangaja.presentation.otp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.ActivityOtpBinding
import com.andc4.terbangaja.presentation.login.LoginActivity
import com.andc4.terbangaja.utils.proceedWhen
import com.otpview.OTPListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpActivity : AppCompatActivity() {
    private val binding: ActivityOtpBinding by lazy {
        ActivityOtpBinding.inflate(layoutInflater)
    }
    private val viewModel: OtpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListener()
        setTimer(60000)
        setOtpView()
    }

    private fun setOtpView() {
        val email = intent.getStringExtra("email")
        val otpView = binding.otpView
        otpView.requestFocusOTP()
        otpView.otpListener =
            object : OTPListener {
                override fun onInteractionListener() {
                }

                override fun onOTPComplete(otp: String) {
                    verifyOTP(email.orEmpty(), otp)
                }
            }
    }

    private fun setTimer(time: Long) {
        val timer =
            object : CountDownTimer(time, 999) {
                @SuppressLint("StringFormatMatches")
                override fun onTick(millisUntilFinished: Long) {
                    binding.tvSendCodeAgain.isVisible = false
                    binding.tvCountdown.isVisible = true
                    binding.tvCountdown.text =
                        getString(R.string.text_send_otp_timer, (millisUntilFinished / 1000))
                }

                override fun onFinish() {
                    binding.tvCountdown.isVisible = false
                    binding.tvSendCodeAgain.isVisible = true
                    binding.tvSendCodeAgain.text = getString(R.string.text_send_otp)
                }
            }
        timer.start()
    }

    private fun setClickListener() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.tvSendCodeAgain.setOnClickListener {
            resendOTP()
        }
    }

    private fun resendOTP() {
        viewModel.doResend().observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                    setTimer(60000)
                },
                doOnError = {
                    it.exception?.let {
                        Toast.makeText(this, it.cause?.message, Toast.LENGTH_SHORT).show()
                    }
                    setTimer(60000)
                },
            )
        }
    }

    private fun verifyOTP(
        email: String,
        otp: String,
    ) {
        viewModel.doVerify(email, otp).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                    navigateToLogin()
                },
                doOnError = {
                    it.exception?.let {
                        Toast.makeText(this, it.cause?.message, Toast.LENGTH_SHORT).show()
                    }
                },
            )
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }
}
