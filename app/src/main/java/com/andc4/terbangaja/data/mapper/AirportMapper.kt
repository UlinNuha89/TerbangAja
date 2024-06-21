package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.data.source.network.model.BasePaging
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.AirportsData

fun BaseResponse<AirportsData>.toAirport() =
    Airport(
        id = this.data?.id ?: 0,
        name = this.data?.name.orEmpty(),
        city = this.data?.city.orEmpty(),
        country = this.data?.country.orEmpty(),
        imgUrl = this.data?.imgUrl.orEmpty(),
    )

fun AirportsData?.toAirport() =
    Airport(
        id = this?.id ?: 0,
        name = this?.name.orEmpty(),
        city = this?.city.orEmpty(),
        country = this?.country.orEmpty(),
        imgUrl = this?.imgUrl.orEmpty(),
    )

fun BaseResponse<BasePaging<List<AirportsData>>>.toAirports() =
    this.data?.result?.map {
        Airport(
            id = it.id,
            name = it.name,
            city = it.city,
            country = it.country,
            imgUrl = it.imgUrl,
        )
    } ?: listOf()
