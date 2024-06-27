package com.andc4.terbangaja.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andc4.terbangaja.data.source.local.database.dao.SearchDao
import com.andc4.terbangaja.data.source.local.database.entity.SearchEntity

@Database(
    entities = [SearchEntity::class],
    version = 5,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao

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
