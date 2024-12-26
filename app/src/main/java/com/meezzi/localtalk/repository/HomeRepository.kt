package com.meezzi.localtalk.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.meezzi.localtalk.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class HomeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) {

    suspend fun getCurrentLocation(): String {

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
            val location: Location? = fusedLocationProviderClient.lastLocation.await()
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