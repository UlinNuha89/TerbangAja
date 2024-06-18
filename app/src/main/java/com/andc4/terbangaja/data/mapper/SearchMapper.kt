package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.data.source.local.database.entity.SearchEntity

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
