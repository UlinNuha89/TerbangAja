package com.andc4.terbangaja.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andc4.terbangaja.data.source.local.database.dao.SearchDao
import com.andc4.terbangaja.data.source.local.database.dao.SearchHistoryDao
import com.andc4.terbangaja.data.source.local.database.entity.SearchEntity
import com.andc4.terbangaja.data.source.local.database.entity.SearchHistoryEntity

@Database(
    entities = [SearchEntity::class, SearchHistoryEntity::class],
    version = 6,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        private const val DB_NAME = "terbangAja.db"

        fun createInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_NAME,
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}
