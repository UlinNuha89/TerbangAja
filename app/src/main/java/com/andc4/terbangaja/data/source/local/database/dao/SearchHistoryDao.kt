package com.andc4.terbangaja.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andc4.terbangaja.data.source.local.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM HISTORY")
    fun getAllSearchHistory(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(item: SearchHistoryEntity): Long

    @Delete
    suspend fun deleteSearchHistory(myList: SearchHistoryEntity): Int

    @Query("DELETE FROM HISTORY")
    suspend fun deleteAllHistory()
}
