package com.example.pppbuas.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ticket(
    val travelId: String?,
    val userId: String?,
    val classId: String?,
    val bookTimestamp: Long?
) : Parcelable {
    constructor() : this(null, null, null, null)
}
