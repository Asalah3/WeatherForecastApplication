package com.example.weatherforecastapplication.view.alerts

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.weatherforecastapplication.PERMISSION_ID
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model.FavouritePlace
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.view.favourite.FavouriteViewModel
import com.example.weatherforecastapplication.view.favourite.FavouriteViewModelFactory
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class AlertMapFragment : DialogFragment() , OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_WeatherForecastApplication_Dialog)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.PHOTO_METADATAS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.let {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(it)
                            .title(place.name)
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))
                }
            }

            override fun onError(status: Status) {
                Toast.makeText(requireContext(), status.toString(), Toast.LENGTH_SHORT).show();

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return inflater.inflate(R.layout.fragment_alert_map, container, false)
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf<String>(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                Toast.makeText(requireContext(), "Turn On Location", Toast.LENGTH_LONG).show()
                val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(settingsIntent)
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
        mLocationRequest.interval = 200000
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        @SuppressLint("SuspiciousIndentation")
        override fun onLocationResult(p0: LocationResult) {
            if (p0 != null) {
                super.onLocationResult(p0)
            }
            val mlastLocation: Location? = p0?.getLastLocation()
            val latlang = LatLng(mlastLocation?.latitude ?: 0.0, mlastLocation?.longitude ?: 0.0)
            mMap.addMarker(
                MarkerOptions()
                    .position(latlang)
                    .title("My Location")
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlang))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latlang))
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))

            mMap.setOnMapLongClickListener { latLng ->
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("My Location")
                )
                val geoCoder = Geocoder(requireContext())
                val myAddress = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                checkToSave(myAddress?.get(0)?.adminArea.toString())
            }
        }
    }

    fun registerObserver(cityName:String/*,lat:Long,long: Long*/){

        val navController = findNavController()
        val navBackStackEntry = navController.previousBackStackEntry

        val observer = LifecycleEventObserver { _, event ->

        }
        navBackStackEntry?.lifecycle?.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry?.savedStateHandle?.set("cityName",cityName)
                /*navBackStackEntry?.savedStateHandle?.set("lat",lat)
                navBackStackEntry?.savedStateHandle?.set("long",long)*/
                navBackStackEntry?.lifecycle?.removeObserver(observer)
            }
        })
    }
    fun checkToSave(placeName: String) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

        alert.setTitle("Alert Map")
        alert.setMessage("Do You want to save ${placeName} place on Alert")
        alert.setPositiveButton("Save") { _: DialogInterface, _: Int ->
            registerObserver(placeName)
            NavHostFragment.findNavController(this).popBackStack()
        }
        val dialog = alert.create()
        dialog.show()

    }
    override fun onMapReady(map: GoogleMap) {
        getLastLocation()
        mMap = map
    }
}