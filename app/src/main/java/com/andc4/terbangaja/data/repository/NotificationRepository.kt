package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.NotificationDataSource
import com.andc4.terbangaja.data.mapper.toNotifications
import com.andc4.terbangaja.data.model.Notification
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

interface NotificationRepository {
    fun getNotification(): Flow<ResultWrapper<List<Notification>>>

    fun updateNotification(id: Int): Flow<ResultWrapper<String>>
}

class NotificationRepositoryImpl(private val dataSource: NotificationDataSource) :
    NotificationRepository {
    override fun getNotification(): Flow<ResultWrapper<List<Notification>>> {
        return proceedFlow {
            val response = dataSource.getNotifications()
            response.data?.toNotifications() ?: listOf()
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun updateNotification(id: Int): Flow<ResultWrapper<String>> {
        return proceedFlow {
            dataSource.updateNotification(id).message
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }
}
