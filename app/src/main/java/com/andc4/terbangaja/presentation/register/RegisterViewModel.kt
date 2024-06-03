package com.andc4.terbangaja.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import com.andc4.terbangaja.data.source.local.pref.AuthPreference
import kotlinx.coroutines.Dispatchers

class RegisterViewModel(
    private val repository: AuthRepository,
    private val pref: AuthPreference,
) : ViewModel() {
    fun setToken(token: String) = pref.setToken(token)

    fun doRegister(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
    ) = repository
        .doRegister(
            name = name,
            email = email,
            phoneNumber = phoneNumber,
            password = password,
        ).asLiveData(Dispatchers.IO)
}
