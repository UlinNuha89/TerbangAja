package com.andc4.terbangaja.data.mapper

import com.andc4.terbangaja.data.model.Seats
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.SeatData

fun BaseResponse<List<SeatData>>?.toSeat() =
    this?.data?.map {
        Seats(
            id = it.id,
            seatNo = it.seatNo,
            booked = it.booked,
        )
    } ?: listOf()

fun List<Seats>?.toSeatsString(): String {
    var seatMap = ""
    var line = ""
    for (i in 0 until this?.size!!) {
        val seat = this[i]
        val booked = seat.booked
        val seatChar = getSeatChar(booked)
        line += seatChar
        // Add 'R' after every 3 characters
        if (line.length == 3) {
            line += '_'
        }
        // When the line reaches 7 characters (6 seats + 1 'R') or it's the last seat
        if (line.length == 7 || i == this.size - 1) {
            seatMap += "/$line"
            line = ""
        }
    }
    return seatMap
}

fun getSeatChar(booked: Boolean): Char {
    return if (booked) 'U' else 'A'
}

fun List<Seats>?.toTitleString(): List<String> {
    val seatsList = this
    val title = mutableListOf<String>()
    for (i in 0 until seatsList?.size!! step 6) {
        title.add("/")
        title.add(seatsList[i].seatNo)
        title.add(seatsList[i + 1].seatNo)
        title.add(seatsList[i + 2].seatNo)
        title.add("_")
        title.add(seatsList[i + 3].seatNo)
        title.add(seatsList[i + 4].seatNo)
        title.add(seatsList[i + 5].seatNo)
    }
    return title
}

fun List<Seats>?.toListInt() =
    this?.map {
        it.id
    } ?: listOf()
