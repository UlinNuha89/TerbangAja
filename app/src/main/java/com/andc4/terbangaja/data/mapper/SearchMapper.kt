package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Destination
import com.andc4.terbangaja.data.source.local.database.entity.SearchEntity

fun SearchEntity?.toDestination() =
    Destination(
        id = this?.id ?: 0,
        item = this?.item.orEmpty(),
    )

fun List<SearchEntity>?.toDestinationList() = this?.map { it.toDestination() } ?: listOf()
