package com.example.pppbuas.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookedAmenity(
    val ticketId: String?,
    val amenityId: String?
) : Parcelable {
    constructor() : this(null, null)
}
