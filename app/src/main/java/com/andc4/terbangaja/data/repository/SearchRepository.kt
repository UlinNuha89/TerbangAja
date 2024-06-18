package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.SearchDataSource
import com.andc4.terbangaja.data.mapper.toAirportList
import com.andc4.terbangaja.data.mapper.toSearchEntity
import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceed
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface SearchRepository {
    fun getAllSearch(): Flow<ResultWrapper<List<Airport>>>

    fun insertSearch(item: Airport): Flow<ResultWrapper<Boolean>>

    fun deleteSearch(item: Airport): Flow<ResultWrapper<Boolean>>

    fun deleteAll(): Flow<ResultWrapper<Boolean>>
}

class SearchRepositoryImpl(private val searchDataSource: SearchDataSource) : SearchRepository {
    override fun getAllSearch(): Flow<ResultWrapper<List<Airport>>> {
        return searchDataSource.getAllSearch().map {
            proceed {
                it.toAirportList()
            }
        }.map {
            if (it.payload?.isEmpty() == false) return@map it
            ResultWrapper.Empty(it.payload)
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun deleteSearch(item: Airport): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val affectedRow = searchDataSource.deleteSearch(item.toSearchEntity())
            affectedRow > 0
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun insertSearch(item: Airport): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val affectedRow =
                searchDataSource.insertSearch(
                    item.toSearchEntity(),
                )
            affectedRow > 0
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun deleteAll(): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            searchDataSource.deleteAll()
            true
        }
    }
}
