package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AuthDataSource {
    //    @Throws(exceptionClasses = [Exception::class])
//    suspend fun doLogin(email: String, password: String): Boolean

    @Throws(exceptionClasses = [Exception::class])
    suspend fun doRegister(
        name: String,
        email: String,
        password: String,
        image: MultipartBody.Part? = null,
        phoneNumber: String,
    ): Boolean
}

class AuthDataSourceImpl(private val service: TerbangAjaApiService) : AuthDataSource {
    /* override suspend fun doLogin(email: String, password: String): Call<ResponseBody> {
         val loginRequest = LoginRequest(email, password)
         return service.login(loginRequest)
     }*/
    override suspend fun doRegister(
        name: String,
        email: String,
        password: String,
        image: MultipartBody.Part?,
        phoneNumber: String,
    ): Boolean {
        val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
        val phoneNumberBody = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneNumber)

        val request = service.register(nameBody, emailBody, passwordBody, image, phoneNumberBody)
        return if (request.data != null) {
            true
        } else {
            false
        }
    }
}
