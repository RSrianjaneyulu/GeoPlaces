package com.geocodingmap.place.mvvm

import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geocodingmap.place.databinding.ItemPlaceAdapterBinding
import com.geocodingmap.place.mvvm.model.CommonPlace
import org.apache.commons.lang3.StringUtils
import java.io.IOException
import java.util.*

class PlaceAdapter : RecyclerView.Adapter<PlaceViewHolder>(){

    var placeList = mutableListOf<List<CommonPlace>>()
    var placList = mutableListOf<List<CommonPlace>>();
    var currentDateTime: String = ""
    var latitudeStr: String = ""
    var longitudeStr: String = ""
    fun setWeatherList(place: CommonPlace){
        placList = mutableListOf()
        placList.add(listOf(place))
        this.placeList = placList.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaceAdapterBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        try {
            val placeData = placeList[position]
            if (placeData != null) {
                if (placeData.get(position).place_name != null) {
                    holder.binding.tvIPAPlaceNameDetails.text = "Place Name: " + placeData.get(position).place_name
                }else {
                    holder.binding.tvIPAPlaceNameDetails.text = "Place Name: "
                }
                if (placeData.get(position).place_type != null) {
                    holder.binding.tvIPAPlaceTypeDetails.text = "Place Type: " + placeData.get(position).place_type.get(0)
                }else {
                    holder.binding.tvIPAPlaceTypeDetails.text = "Place Type: "
                }
                if (placeData.get(position).center != null) {
                    latitudeStr = placeData.get(position).center.get(0).toString()
                    longitudeStr = placeData.get(position).center.get(1).toString()
                    if (StringUtils.isNotEmpty(latitudeStr) && StringUtils.isNotEmpty(longitudeStr)) {
                        val latitude: Double? = latitudeStr.toDouble()
                        val longitude: Double? = longitudeStr.toDouble()
                        val geocoder = Geocoder(holder.itemView.context, Locale.getDefault())
                        var addresses: List<Address>? = null
                        try {
                            addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
                            val address: String = addresses!![0].getAddressLine(0)
                            val locality: String = addresses!![0].locality
                            val countryCode: String = addresses!![0].countryCode
                            //val countryName: String = addresses!![0].countryName
                            //val postalCode: String = addresses!![0].postalCode
                            holder.binding.tvIPACentreDetails.setText("Centre: " + "$locality, $countryCode")
                        } catch (ioException: IOException) {
                            ioException.printStackTrace()
                        }
                    }

                }else {
                    holder.binding.tvIPACentreDetails.text = "Centre: "
                }
            }

        }catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    override fun getItemCount(): Int {
        return placeList.size
    }

}
class PlaceViewHolder(val binding: ItemPlaceAdapterBinding) : RecyclerView.ViewHolder(binding.root) {}