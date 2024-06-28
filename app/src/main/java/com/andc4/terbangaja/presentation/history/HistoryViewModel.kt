package com.andc4.terbangaja.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andc4.terbangaja.data.model.BookingHistoryModel
import com.andc4.terbangaja.data.model.SearchHistory
import com.andc4.terbangaja.data.repository.AuthRepository
import com.andc4.terbangaja.data.repository.BookingHistoryRepository
import com.andc4.terbangaja.data.repository.SearchHistoryRepository
import com.andc4.terbangaja.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val authRepository: AuthRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val historyRepository: BookingHistoryRepository,
) : ViewModel() {
    private val _history = MutableLiveData<ResultWrapper<List<BookingHistoryModel>>>()
    val history: LiveData<ResultWrapper<List<BookingHistoryModel>>> get() = _history

    fun getBookingHistories(
        startDate: String?,
        endDate: String?,
        code: String?,
    ) {
        viewModelScope.launch {
            historyRepository.getBookingHistories(startDate, endDate, code).collect { result ->
                _history.value = result
            }
        }
    }

    fun getRecentSearch() = searchHistoryRepository.getAllSearch().asLiveData(Dispatchers.IO)

    fun insertRecentSearch(item: SearchHistory): LiveData<ResultWrapper<Boolean>> {
        return item.let {
            searchHistoryRepository.insertSearch(item).asLiveData(Dispatchers.IO)
        }
    }

    fun deleteRecentSearch(item: SearchHistory): LiveData<ResultWrapper<Boolean>> {
        return item.let {
            searchHistoryRepository.deleteSearch(item).asLiveData(Dispatchers.IO)
        }
    }

    fun isLogin() = authRepository.isLogin().asLiveData(Dispatchers.IO)
}
