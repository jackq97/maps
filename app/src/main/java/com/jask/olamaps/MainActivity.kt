package com.jask.olamaps

import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.ola.maps.mapslibrary.models.OlaLatLng
import com.ola.maps.mapslibrary.models.OlaMapsConfig
import com.ola.maps.mapslibrary.models.OlaMarkerOptions
import com.ola.maps.navigation.ui.v5.MapStatusCallback
import com.ola.maps.navigation.v5.navigation.OlaMapView

class MainActivity : AppCompatActivity(), MapStatusCallback{

    private val viewModel: MapsViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val markerViewOptions = OlaMarkerOptions.Builder()
        .setIconIntRes(R.drawable.ic_location)
        .setMarkerId("1")
        .setIconSize(0.05f)
        .build()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val olaMapView = findViewById<OlaMapView>(R.id.olaMapView)
        val button = findViewById<Button>(R.id.button)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){ permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    false) || permissions.getOrDefault(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,false) -> {
                            Toast.makeText(this, "location access granted", Toast.LENGTH_SHORT).show()

                            if (isLocationEnabled()){
                                val result = fusedLocationClient.getCurrentLocation(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    CancellationTokenSource().token
                                )
                                result.addOnCompleteListener {
                                    val location = "Location: ${it.result}"

                                    olaMapView.updateMarkerView(
                                        OlaMarkerOptions.Builder()
                                            .setMarkerId(markerViewOptions.markerId)
                                            .setPosition(
                                                OlaLatLng(
                                                    latitude = it.result.latitude,
                                                    longitude = it.result.longitude
                                                )
                                            )
                                            .setIconIntRes(markerViewOptions.iconIntRes!!)
                                            .setIconSize(markerViewOptions.iconSize)
                                            .build()
                                    )

                                    viewModel.getAddress(latLng = "${it.result.latitude},${it.result.longitude}"){ result ->
                                        Log.d("TAG", "onCreate: ${result.results[0].formatted_address}")
                                    }
                                    Log.d("TAG", "onCreate: $location")
                                }
                            } else {
                                Toast.makeText(this, "location access denied", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        }

        button.setOnClickListener {
            locationPermissionRequest.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            olaMapView?.moveToCurrentLocation()
        }

        getAccessToken()

        olaMapView?.initialize(
            mapStatusCallback = this,
            olaMapsConfig = OlaMapsConfig.Builder()
                .setApplicationContext(applicationContext)
                .setClientId(R.string.client_id.toString())
                .setMapBaseUrl("https://api.olamaps.io")
                .setInterceptor { chain ->
                    val originalRequest = chain.request()

                    val newRequest = originalRequest.newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${viewModel.accessToken}"
                        )
                        .build()

                    chain.proceed(newRequest)
                }
                .setMinZoomLevel(10.0)
                .setMaxZoomLevel(16.0)
                .setZoomLevel(14.0)
                .build()
        )

    }

    override fun onMapReady() {

    }

    override fun onMapLoadFailed(p0: String?) {
        Log.d("TAG", "onMapLoadFailed: ${viewModel.accessToken}")
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun getAccessToken(){

        viewModel.getAccessToken(
            clientId = "1a8342a1-bc4d-4243-8b6f-7787812ecebf",
            clientSecret = "Dc163ckDW20u4xviH2ytzhhf72rJoPN3",
            onSuccess = {  },
            onFailure = { errorMsg ->
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onMapLoadFailed: $errorMsg")
            }
        )
    }
}