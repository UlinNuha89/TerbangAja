package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.SearchDataSource
import com.andc4.terbangaja.data.mapper.toDestinationList
import com.andc4.terbangaja.data.model.Destination
import com.andc4.terbangaja.data.source.local.database.entity.SearchEntity
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceed
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface SearchRepository {
    fun getAllSearch(): Flow<ResultWrapper<List<Destination>>>

    suspend fun insertSearch(item: String): Flow<ResultWrapper<Boolean>>

    suspend fun deleteAll(): Flow<ResultWrapper<Boolean>>
}

class SearchRepositoryImpl(private val searchDataSource: SearchDataSource) : SearchRepository {
    override fun getAllSearch(): Flow<ResultWrapper<List<Destination>>> {
        return searchDataSource.getAllSearch().map {
            proceed {
                it.toDestinationList()
            }
        }.map {
            if (it.payload?.isEmpty() == false) return@map it
            ResultWrapper.Empty(it.payload)
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(1000)
        }
    }

    override suspend fun insertSearch(item: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val affectedRow =
                searchDataSource.insertSearch(
                    SearchEntity(
                        item = item,
                    ),
                )
            affectedRow > 0
        }
    }

    override suspend fun deleteAll(): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            searchDataSource.deleteAll()
            true
        }
    }
}
