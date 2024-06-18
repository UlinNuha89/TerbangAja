package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.local.database.dao.SearchDao
import com.andc4.terbangaja.data.source.local.database.entity.SearchEntity
import kotlinx.coroutines.flow.Flow

interface SearchDataSource {
    fun getAllSearch(): Flow<List<SearchEntity>>

    suspend fun insertSearch(item: SearchEntity): Long

    suspend fun deleteSearch(item: SearchEntity): Int

    suspend fun deleteAll()
}

class SearchDataSourceImpl(private val dao: SearchDao) : SearchDataSource {
    override fun getAllSearch(): Flow<List<SearchEntity>> = dao.getAllSearch()

    override suspend fun insertSearch(item: SearchEntity): Long = dao.insertSearch(item)

    override suspend fun deleteSearch(item: SearchEntity): Int = dao.deleteSearch(item)

    override suspend fun deleteAll() = dao.deleteAll()
}
