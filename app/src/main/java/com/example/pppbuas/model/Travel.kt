package com.example.pppbuas.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Travel(
    val name: String?,
    val destinationStation: String?,
    val departureStation: String?,
    val price: Double?,
    val departureTime: String?,
    val arrivalTime: String?,
    val date: String?,
    val totalSeats: Int?,
    val bookedSeats: Int?
) : Parcelable {
    constructor() : this(null, null, null, null, null, null, null, null, null)
}
