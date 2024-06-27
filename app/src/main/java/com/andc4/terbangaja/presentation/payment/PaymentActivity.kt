package com.andc4.terbangaja.presentation.payment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.ActivityPaymentBinding
import com.andc4.terbangaja.presentation.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaymentActivity : AppCompatActivity() {
    private val binding: ActivityPaymentBinding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }
    private val viewModel: PaymentViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private var isSuccess = false

    companion object {
        const val EXTRAS_URL = "EXTRAS_URL"

        fun startActivity(
            context: Context,
            url: String,
        ) {
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra(EXTRAS_URL, url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setWebView()
        setOnClick()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        val url = viewModel.getUrl()!!
        binding.webView.webViewClient =
            object : WebViewClient() {
                val pd = ProgressDialog(this@PaymentActivity)

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    val requestUrl = request?.url.toString()
                    return if (requestUrl.contains(viewModel.homeUrl())) {
                        isSuccess = true
                        false
                    } else {
                        false
                    }
                }

                override fun onPageStarted(
                    view: WebView,
                    url: String,
                    favicon: Bitmap?,
                ) {
                    pd.setMessage(getString(R.string.text_Loading))
                    pd.show()
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(
                    view: WebView,
                    url: String,
                ) {
                    pd.dismiss()
                    if (isSuccess) {
                        navigateToMain()
                    }
                    super.onPageFinished(view, url)
                }
            }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadsImagesAutomatically = true
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.webView.loadUrl(url)
    }

    private fun setOnClick() {
        binding.layoutHeader.ivBackHeader.setOnClickListener {
            navigateToMain()
        }
        binding.layoutHeader.tvTitle.text = getString(R.string.title_proses_pembayaran)
    }

    override fun onBackPressed() {
        navigateToMain()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        super.onBackPressed()
    }

    private fun navigateToMain() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }
}
