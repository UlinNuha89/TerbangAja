package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.AirportsData

fun BaseResponse<List<AirportsData>>.toAirport() =
    this.data?.map {
        Airport(
            id = it.id,
            name = it.name,
            city = it.city,
            country = it.country,
            imgUrl = it.imgUrl,
        )
    } ?: listOf()
