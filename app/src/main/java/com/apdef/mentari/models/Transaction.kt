package com.apdef.mentari.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction (
    var time: String="",
    var username: String = "",
    var total: Int = 0
):Parcelable