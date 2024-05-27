package com.andc4.terbangaja.presentation.otp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.ActivityOtpBinding
import com.otpview.OTPListener

class OtpActivity : AppCompatActivity() {
    private val binding: ActivityOtpBinding by lazy {
        ActivityOtpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // sendOTP()
        setClickListener()
        setTimer()
        val otpView = binding.otpView
        otpView.requestFocusOTP()
        otpView.otpListener =
            object : OTPListener {
                override fun onInteractionListener() {
                }

                override fun onOTPComplete(otp: String) {
                    // verifyOTP()
                    Toast.makeText(this@OtpActivity, "your otp is $otp", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setTimer() {
        val timer =
            object : CountDownTimer(60000, 999) {
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
            // sendOTPAgain()
            setTimer()
        }
    }
}
