package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Airline
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.AirlinesData

fun BaseResponse<List<AirlinesData>>.toAirline() =
    this.data?.map {
        Airline(
            id = it.id,
            name = it.name,
            code = it.code,
            baggage = it.baggage,
            cabinBaggage = it.cabinBaggage,
        )
    } ?: listOf()
