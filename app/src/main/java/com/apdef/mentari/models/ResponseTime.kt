package com.apdef.mentari.models

import com.google.gson.annotations.SerializedName

data class ResponseTime (

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("countryCode")
    val countryCode: String? = null,

    @field:SerializedName("countryName")
    val countryName: String? = null,

    @field:SerializedName("regionName")
    val regionName: String? = null,

    @field:SerializedName("cityName")
    val cityName: String? = null,

    @field:SerializedName("zoneName")
    val zoneName: String? = null,

    @field:SerializedName("abbreviation")
    val abbreviation: String? = null,

    @field:SerializedName("gmtOffset")
    val gmtOffset: Int? = null,

    @field:SerializedName("dst")
    val dst: String? = null,

    @field:SerializedName("zoneStart")
    val zoneStart: Int? = null,

    @field:SerializedName("zoneEnd")
    val zoneEnd: Int? = null,

    @field:SerializedName("nextAbbreviation")
    val nextAbbreviation: String? = null,

    @field:SerializedName("timestamp")
    val timestamp: Int? = null,

    @field:SerializedName("formatted")
    val formatted: String? = null
)

//"status": "OK",
//"message": "",
//"countryCode": "US",
//"countryName": "United States",
//"regionName": "",
//"cityName": "",
//"zoneName": "America\/Chicago",
//"abbreviation": "CDT",
//"gmtOffset": -18000,
//"dst": "1",
//"zoneStart": 1583654400,
//"zoneEnd": 1604214000,
//"nextAbbreviation": "CST",
//"timestamp": 1593332229,
//"formatted": "2020-06-28 08:17:09"