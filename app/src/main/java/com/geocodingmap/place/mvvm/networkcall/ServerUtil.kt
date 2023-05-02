package com.geocodingmap.place.mvvm.networkcall

class ServerUtil {
    companion object {
        val serverBaseUrl = "https://api.mapbox.com/"
        val getPlaceDetailsURL = serverBaseUrl + "geocoding/v5/mapbox.places/"
    }
}