package com.andc4.terbangaja.presentation.payment

import android.os.Bundle
import androidx.lifecycle.ViewModel

class PaymentViewModel(
    private val extras: Bundle,
) : ViewModel() {
    fun getUrl() = extras.getString(PaymentActivity.EXTRAS_URL)
}
