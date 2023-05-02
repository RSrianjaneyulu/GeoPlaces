package com.geocodingmap.place.mvvm

import com.geocodingmap.place.mvvm.model.CommonPlace
import com.geocodingmap.place.mvvm.networkcall.ServerUtil
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceService {

    //@GET
    //suspend fun getPlaceData(@Query("access_token") apikey : String): Response<CommonPlace>

    @GET(".json?")
    suspend fun getPlaceData(@Query("PLACE-NAME") placename : String, @Query("access_token") apikey : String): Response<CommonPlace>

    companion object {
        var placeService: PlaceService? = null
        fun getInstance(): PlaceService {
            if (placeService == null){
                val retrofit = Retrofit.Builder().baseUrl(ServerUtil.getPlaceDetailsURL).addConverterFactory(
                    GsonConverterFactory.create()).build()
                placeService = retrofit.create(PlaceService::class.java)
            }
            return placeService!!
        }

    }
}