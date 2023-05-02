package com.geocodingmap.place.mvvm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geocodingmap.place.R
import com.geocodingmap.place.databinding.ActivityPlaceBinding
import com.geocodingmap.place.mvvm.helperclasses.Helper
import org.apache.commons.lang3.StringUtils

class PlaceSearchActivity : AppCompatActivity(){
    var placeName: String = ""
    var apikey: String = "pk.eyJ1Ijoic3Jpa2FudGhiIiwiYSI6ImNrcnlsN2d2MjExYWwydm44ZWl2eWIzaW4ifQ.AqIktKkMjuUOdT8pfkviOQ"
    lateinit var placeViewModel: PlaceViewModel
    private val placeAdapter = PlaceAdapter()
    lateinit var placeBinding: ActivityPlaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placeBinding = ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(placeBinding.root)

        //Location Enable Permission
        try {
            if (ContextCompat.checkSelfPermission(this@PlaceSearchActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) !==
                PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@PlaceSearchActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this@PlaceSearchActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                } else {
                    ActivityCompat.requestPermissions(this@PlaceSearchActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
        //Search PlaceName
        placeBinding.etAPEnterPlaceName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(text: CharSequence, start: Int,
                                       before: Int, count: Int) {
                placeName = text.toString().trim();
                if(StringUtils.isNotEmpty(placeName)){
                    placeBinding.tvAPPlaceNameLabel.isVisible = true
                }else {
                    placeBinding.tvAPPlaceNameLabel.isVisible = false
                }
            }
        })
        placeBinding.ivAPPlaceNameSearchIcon.setOnClickListener({
            placeNameValidationCheck()
        })
    }
    override fun onResume() {
        super.onResume()
    }
    //placename validation check
    fun placeNameValidationCheck() {
        placeName = placeBinding.etAPEnterPlaceName.getText().toString().trim().toLowerCase()
        if (!StringUtils.isNotEmpty(placeName)) {
            placeBinding.etAPEnterPlaceName.requestFocus()
            placeBinding.etAPEnterPlaceName.setError(getString(R.string.placename_required))
            return
        }else if (placeName.length < 3){
            placeBinding.etAPEnterPlaceName.requestFocus()
            placeBinding.etAPEnterPlaceName.setError(getString(R.string.valid_placenamecharacters))
            return
        } else {
            placeBinding.etAPEnterPlaceName.setError(null)
        }
         passingDataToServerCall()
    }

    //Passing data to API call
    fun passingDataToServerCall(){
        placeBinding.pbAPProgressDialog.visibility = View.VISIBLE
        placeBinding.rvAPPlacesList.adapter = placeAdapter
        placeViewModel = ViewModelProvider(this)[PlaceViewModel::class.java]
        //Calling Coroutines function
        placeViewModel.getPlaceDetails(placeName, apikey)
        placeViewModel.observePlaceLiveData().observe(this, Observer {
            placeAdapter.setWeatherList(it)
        })
        placeViewModel.errorMessage.observe(this, Observer {
            Helper.showShortToast(this, it)
        })
        placeViewModel.loading.observe(this, Observer {
            if (it){
                placeBinding.pbAPProgressDialog.visibility = View.VISIBLE
            }else {
                placeBinding.pbAPProgressDialog.visibility= View.GONE
            }
        })
    }


}