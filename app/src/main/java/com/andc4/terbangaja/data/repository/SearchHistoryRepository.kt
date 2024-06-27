package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.SearchHistoryDataSource
import com.andc4.terbangaja.data.mapper.toSearchHistoryModelList
import com.andc4.terbangaja.data.mapper.toSearchHistorynEntity
import com.andc4.terbangaja.data.model.SearchHistory
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceed
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface SearchHistoryRepository {
    fun getAllSearch(): Flow<ResultWrapper<List<SearchHistory>>>

    fun insertSearch(item: SearchHistory): Flow<ResultWrapper<Boolean>>

    fun deleteSearch(item: SearchHistory): Flow<ResultWrapper<Boolean>>

    fun deleteAll(): Flow<ResultWrapper<Boolean>>
}

class SearchHistoryRepositoryImpl(private val searchDataSource: SearchHistoryDataSource) : SearchHistoryRepository {
    override fun getAllSearch(): Flow<ResultWrapper<List<SearchHistory>>> {
        return searchDataSource.getAllSearch().map {
            proceed {
                it.toSearchHistoryModelList()
            }
        }.map {
            if (it.payload?.isEmpty() == false) return@map it
            ResultWrapper.Empty(it.payload)
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun deleteSearch(item: SearchHistory): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val affectedRow = searchDataSource.deleteSearch(item.toSearchHistorynEntity())
            affectedRow > 0
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun insertSearch(item: SearchHistory): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val affectedRow =
                searchDataSource.insertSearch(
                    item.toSearchHistorynEntity(),
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
