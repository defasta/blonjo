package com.apdef.mentari.models

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "product")
data class Product (
    @ColumnInfo(name = "images")
    var images: String? ="",

    @PrimaryKey
    @NonNull
    var id: String ="",

    @ColumnInfo(name = "name")
    var name: String? ="",

    @ColumnInfo(name = "time")
    var time: String? ="",

    @ColumnInfo(name = "price")
    var price: Int?=null,

    @ColumnInfo(name = "count")
    var count: Int? = null
):Parcelable