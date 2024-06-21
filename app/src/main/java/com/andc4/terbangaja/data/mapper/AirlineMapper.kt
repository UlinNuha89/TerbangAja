package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Airline
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.AirlinesData

fun BaseResponse<AirlinesData>.toAirline() =
    Airline(
        id = this.data?.id ?: 0,
        name = this.data?.name.orEmpty(),
        code = this.data?.code.orEmpty(),
        baggage = this.data?.baggage ?: 0,
        cabinBaggage = this.data?.cabinBaggage ?: 0,
        imgUrl = this.data?.imgUrl.orEmpty(),
    )

fun AirlinesData?.toAirline() =
    Airline(
        id = this?.id ?: 0,
        name = this?.name.orEmpty(),
        code = this?.code.orEmpty(),
        baggage = this?.baggage ?: 0,
        cabinBaggage = this?.cabinBaggage ?: 0,
        imgUrl = this?.imgUrl.orEmpty(),
    )

fun BaseResponse<List<AirlinesData>>.toAirlines() =
    this.data?.map {
        Airline(
            id = it.id,
            name = it.name,
            code = it.code,
            baggage = it.baggage,
            cabinBaggage = it.cabinBaggage,
            imgUrl = it.imgUrl,
        )
    } ?: listOf()
