package com.example.pppbuas.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravelClass(
    val name: String?,
    val description: String?,
    val price: Double?
) : Parcelable {
    constructor() : this(null, null, null)
}
