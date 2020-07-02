package com.apdef.mentari.api

import com.apdef.mentari.models.ResponseTime
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("get-time-zone")
    fun getTime(
        @Query("key") key:String,
        @Query("format") format:String,
        @Query("by") by: String,
        @Query("zone") zone:String
    ): Call<ResponseTime>
}