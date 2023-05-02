package com.geocodingmap.place.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geocodingmap.place.mvvm.model.CommonPlace
import kotlinx.coroutines.*

class PlaceViewModel : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val placeList = MutableLiveData<CommonPlace>()
    var job: Job? = null
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    //Using Coroutines
    fun getPlaceDetails(placeName: String, apikey: String) {
        job = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val response = PlaceService.getInstance().getPlaceData(placeName, apikey)
            withContext(Dispatchers.Main) {
                try {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                placeList.postValue(response.body()!!)
                                loading.value = false
                            }else {
                                return@withContext
                            }
                        } else {
                            onError("Error : ${response.message()}")
                        }
                    }
                }catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
    //Error message
    private fun onError(message: String){
        errorMessage.value = message
        loading.value = false
    }
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
    //observe the live data with the help of the Observer() function
    fun observePlaceLiveData() : LiveData<CommonPlace> {
        return placeList
    }
}