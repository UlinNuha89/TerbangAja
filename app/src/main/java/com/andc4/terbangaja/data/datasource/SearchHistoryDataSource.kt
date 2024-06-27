package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.local.database.dao.SearchHistoryDao
import com.andc4.terbangaja.data.source.local.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SearchHistoryDataSource {
    fun getAllSearch(): Flow<List<SearchHistoryEntity>>

    suspend fun insertSearch(item: SearchHistoryEntity): Long

    suspend fun deleteSearch(item: SearchHistoryEntity): Int

    suspend fun deleteAll()
}

class SearchHistoryDataSourceImpl(private val dao: SearchHistoryDao) : SearchHistoryDataSource {
    override fun getAllSearch(): Flow<List<SearchHistoryEntity>> = dao.getAllSearchHistory()

    override suspend fun insertSearch(item: SearchHistoryEntity): Long = dao.insertSearchHistory(item)

    override suspend fun deleteSearch(item: SearchHistoryEntity): Int = dao.deleteSearchHistory(item)

    override suspend fun deleteAll() = dao.deleteAllHistory()
}
