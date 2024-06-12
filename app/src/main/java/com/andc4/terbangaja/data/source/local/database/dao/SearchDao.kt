package com.andc4.terbangaja.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andc4.terbangaja.data.source.local.database.entity.SearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query("SELECT * FROM SEARCH")
    fun getAllSearch(): Flow<List<SearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(item: SearchEntity): Long

    @Query("DELETE FROM SEARCH")
    suspend fun deleteAll()
}
