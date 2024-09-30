package com.meezzi.localtalk.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.meezzi.localtalk.R
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Locale

class HomeRepository(private val context: Context) {

    suspend fun getCurrentLocation(): String {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return context.getString(R.string.permission_header_content)
        }

        try {
            val location: Location? = fusedLocationClient.lastLocation.await()
            if (location != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                return addresses?.get(0)?.let {
                    "${it.adminArea} ${it.subLocality}"
                }.orEmpty()
            } else {
                return context.getString(R.string.permission_error_location)
            }
        } catch (e: IOException) {
            return context.getString(R.string.permission_error_location)
        }
    }
}