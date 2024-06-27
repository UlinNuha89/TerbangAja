package com.andc4.terbangaja.presentation.payment

import android.os.Bundle
import androidx.lifecycle.ViewModel

class PaymentViewModel(
    private val extras: Bundle,
) : ViewModel() {
    fun getUrl() = extras.getString(PaymentActivity.EXTRAS_URL)

    fun homeUrl() = "https://terbangaja-binar-9v1x553z5-jevinleons-projects.vercel.app/"
}
