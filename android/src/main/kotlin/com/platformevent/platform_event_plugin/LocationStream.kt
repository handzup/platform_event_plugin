package com.platformevent.platform_event_plugin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import io.flutter.plugin.common.EventChannel
import android.os.Bundle





class LocationStreamHandler(private  var activity: Activity?):EventChannel.StreamHandler {

    private var eventSink: EventChannel.EventSink? = null

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
        startListeningNetworkChanges()
        println("start listen")

    }

    override fun onCancel(arguments: Any?) {
        stopListeningNetworkChanges()
        println("end listen")

    }
    private  fun startListeningNetworkChanges(){
        val manager =  activity?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                    42)
            return
        }
        println("start listen sink")

        manager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,10f,locationListenerGPS)
    }
    private var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val latitude = location.latitude
            val longitude = location.longitude
            val msg = "New Latitude: " + latitude + "New Longitude: " + longitude
            println(msg)
            eventSink?.success(msg)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
    private fun stopListeningNetworkChanges() {
        val manager =  activity?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        manager?.removeUpdates(locationListenerGPS)
        println("remove")
    }
}

