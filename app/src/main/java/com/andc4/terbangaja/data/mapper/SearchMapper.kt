package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.data.model.SearchHistory
import com.andc4.terbangaja.data.source.local.database.entity.SearchEntity
import com.andc4.terbangaja.data.source.local.database.entity.SearchHistoryEntity

fun SearchEntity?.toAirport() =
    Airport(
        id = this?.id ?: 0,
        name = this?.name.orEmpty(),
        city = this?.city.orEmpty(),
        country = this?.country.orEmpty(),
        imgUrl = this?.imgUrl.orEmpty(),
    )

fun Airport?.toSearchEntity() =
    SearchEntity(
        id = this?.id ?: 0,
        name = this?.name.orEmpty(),
        city = this?.city.orEmpty(),
        country = this?.country.orEmpty(),
        imgUrl = this?.imgUrl.orEmpty(),
    )

fun List<SearchEntity>?.toAirportList() = this?.map { it.toAirport() } ?: listOf()

fun SearchHistoryEntity?.toSearchHistoryModel() =
    SearchHistory(
        id = this?.id ?: 0,
        userId = this?.userId ?: 0,
        code = this?.code.orEmpty(),
        departure = this?.departure.orEmpty(),
        returnflight = this?.returnFlight.orEmpty(),
    )

fun SearchHistory?.toSearchHistorynEntity() =
    SearchHistoryEntity(
        id = this?.id ?: 0,
        userId = this?.userId ?: 0,
        code = this?.code.orEmpty(),
        departure = this?.departure.orEmpty(),
        returnFlight = this?.returnflight.orEmpty(),
    )

fun List<SearchHistoryEntity>?.toSearchHistoryModelList() = this?.map { it.toSearchHistoryModel() } ?: listOf()
