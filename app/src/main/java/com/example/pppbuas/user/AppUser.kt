package com.example.pppbuas.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppUser(
    val userId: String?,
    val email: String?,
    val role: String?,
    val fullName: String?,
    val phoneNumber: String?,
    val nim: String?
) : Parcelable {
    constructor() : this(null, null, null, null, null, null)
}
