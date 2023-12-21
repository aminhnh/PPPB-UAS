package com.example.pppbuas.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ticket(
    val travelId: String?,
    val userId: String?,
    val classId: String?,
    val seatCount: String?,
    val bookTimestamp: Long?,
    val totalPrice: Double?
) : Parcelable {
    constructor() : this(null, null, null, null, null, null)
}
