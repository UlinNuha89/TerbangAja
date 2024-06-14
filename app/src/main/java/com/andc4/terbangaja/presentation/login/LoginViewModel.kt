package com.andc4.terbangaja.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun doLogin(
        email: String,
        password: String,
    ) = authRepository.doLogin(email = email, password = password).asLiveData(Dispatchers.IO)

//    private val _loginState = MutableStateFlow<ResultWrapper<String>>(ResultWrapper.Idle())
//    val loginState: StateFlow<ResultWrapper<String>> = _loginState
//
//        fun doLogin(email: String, password: String) {
//            viewModelScope.launch {
//                authRepository.doLogin(email, password)
//                    .catch { e ->
//                        _loginState.value = ResultWrapper.Error(Exception(e.message))
//                    }
//                    .collect { result ->
//                        _loginState.value = result
//                    }
//            }
//        }
}
