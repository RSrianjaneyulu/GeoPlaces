package com.geocodingmap.place.mvvm.helperclasses

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Helper {

    companion object {

        private var mContext: Context? = null
        private val df: SimpleDateFormat? = null

        fun Helper(context: Context?) {
            mContext = context
        }

        //Toast Message
        fun showShortToast(ctx: Context?, message: String?) {
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
        }

        //Chcek internet connection
        fun checkInternetConnection(context: Context): Boolean {
            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connMgr.activeNetworkInfo
            if (activeNetworkInfo != null) { // connected to the internet
                showShortToast(context, activeNetworkInfo.typeName);
                return if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    true
                } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    true
                } else {
                    false
                }
            } else {
                showShortToast(context, "Internet Connection is Required");
            }
            return false
        }
    }
}