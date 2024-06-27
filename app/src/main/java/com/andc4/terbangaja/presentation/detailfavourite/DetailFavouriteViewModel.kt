package com.andc4.terbangaja.presentation.detailfavourite

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.SeatClass

class DetailFavouriteViewModel(
    private val extras: Bundle,
) : ViewModel() {
    fun getData() = extras.getParcelable<Flight>(DetailFavouriteActivity.EXTRAS_ITEM_FLIGHT)

    fun getSeatClass(): SeatClass {
        return SeatClass("Economy", "economy")
    }
}
