package com.example.pppbuas.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travel_table")
data class TravelEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "destination_station")
    val destinationStation: String?,

    @ColumnInfo(name = "departure_station")
    val departureStation: String?,

    @ColumnInfo(name = "price")
    val price: Double?,

    @ColumnInfo(name = "departure_time")
    val departureTime: String?,

    @ColumnInfo(name = "arrival_time")
    val arrivalTime: String?,

    @ColumnInfo(name = "date")
    val date: String?,

    @ColumnInfo(name = "total_seats")
    val totalSeats: Int?,

    @ColumnInfo(name = "booked_seats")
    val bookedSeats: Int?
)