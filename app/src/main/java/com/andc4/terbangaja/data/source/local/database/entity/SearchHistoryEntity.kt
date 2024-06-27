package com.andc4.terbangaja.data.source.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "History")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "userid")
    var userId: Int? = null,
    @ColumnInfo(name = "name")
    val code: String,
    @ColumnInfo(name = "departure")
    val departure: String,
    @ColumnInfo(name = "returnFlight")
    val returnFlight: String?,
)
