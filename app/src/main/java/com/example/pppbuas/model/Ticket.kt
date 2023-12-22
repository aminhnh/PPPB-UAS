package com.example.pppbuas.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ticket(
    val travelId: String?,
    val userId: String?,
    val travelClass: String?,
    val seatCount: Int?,
    val bookTimestamp: Long?,
    val totalPrice: String?
) : Parcelable {
    constructor() : this(null, null, null, null, null, null)
}
