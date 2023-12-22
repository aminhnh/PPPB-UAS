package com.example.pppbuas.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Station(
    val name: String?,
    val city: String?
) : Parcelable {
    constructor() : this(null, null)
}
