package com.apdef.mentari.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var username: String? ="",
    var email: String? = "",
    var saldo: Int? = 0,
    var token: String? =""
):Parcelable
