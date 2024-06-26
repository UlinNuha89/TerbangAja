package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.notification.Notification
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson

interface NotificationDataSource {
    suspend fun getNotifications(): BaseResponse<List<Notification>>

    suspend fun updateNotification(id: Int): BaseResponse<Notification>
}

class NotificationDataSourceImpl(private val service: TerbangAjaApiService) :
    NotificationDataSource {
    override suspend fun getNotifications(): BaseResponse<List<Notification>> {
        try {
            val response = service.getNotifications()
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                throw Exception(errorResponse.message)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateNotification(id: Int): BaseResponse<Notification> {
        try {
            val response = service.updateNotification(id)
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                throw Exception(errorResponse.message)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
