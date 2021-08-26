package com.platformevent.platform_event_plugin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import io.flutter.embedding.android.FlutterActivity

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/** PlatformEventPlugin */
class PlatformEventPlugin : FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler,
    ActivityAware, ActivityCompat.OnRequestPermissionsResultCallback {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: EventChannel
    private var applicationContext: Context? = null
    private var activity: FlutterActivity? = null
    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.applicationContext = flutterPluginBinding.applicationContext
        channel = EventChannel(flutterPluginBinding.binaryMessenger, "locationStatusStream")
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setStreamHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

    }

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

    private fun startListeningNetworkChanges() {
        println("before permission")


       if( !checkPermission()) return
        val manager =
            applicationContext?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        manager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            500,
            10f,
            locationListenerGPS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        println("on req callback")
        when (requestCode) {
            1 -> {
                println("granted")

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                }
            }
        }
      }

    private fun checkPermission(): Boolean {
         if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("permission")
            requestPermissions(
                activity!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return false

        } else {
            return true
        }
    }

    private var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val latitude = location.latitude
            val longitude = location.longitude
            val msg = "lat: " + latitude + "long: " + longitude
            println(msg)
            eventSink?.success(msg)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun stopListeningNetworkChanges() {
        val manager =
            applicationContext?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        manager?.removeUpdates(locationListenerGPS)
        println("remove")
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity as FlutterActivity
        channel.setStreamHandler(this)

    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity as FlutterActivity
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }
}
