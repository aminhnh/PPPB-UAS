package com.example.pppbuas.room

import com.example.pppbuas.model.Travel

object TravelExtensions {
    fun TravelEntity.toTravel(): Travel {
        return Travel(
            name = this.name,
            destinationStation = this.destinationStation,
            departureStation = this.departureStation,
            price = this.price,
            departureTime = this.departureTime,
            arrivalTime = this.arrivalTime,
            date = this.date,
            totalSeats = this.totalSeats,
            bookedSeats = this.bookedSeats
        )
    }

}