package com.example.pppbuas.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val name: String?
) : Parcelable {
    constructor() : this(null)
}
