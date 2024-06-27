package com.andc4.terbangaja.presentation.detailhistory

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.model.BookingHistoryModel

class DetailHistoryViewModel(private val extras: Bundle?) : ViewModel() {
    val history = extras?.getParcelable<BookingHistoryModel>(DetailHistoryActivity.EXTRA_HISTORY)

    fun calculatePassengerPrices(history: BookingHistoryModel?): Pair<Int?, Int?> {
        val adultCount = history?.adultCount ?: 0
        val childCount = history?.childCount ?: 0
        val babyCount = history?.babyCount ?: 0
        val totalPassengers = adultCount + childCount + babyCount
        val totalAmount = history?.priceAmount
        val passengersExcludingBabies = adultCount + childCount
        val pricePerPassengerExcludingBabies =
            if (passengersExcludingBabies > 0) {
                totalAmount?.div(passengersExcludingBabies)
            } else {
                0
            }
        val adultPrice = pricePerPassengerExcludingBabies?.times(adultCount)
        val childPrice = pricePerPassengerExcludingBabies?.times(childCount)
        return Pair(adultPrice, childPrice)
    }
}
