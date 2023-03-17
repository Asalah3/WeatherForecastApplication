package com.example.weatherforecastapplication.view.favourite

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.PERMISSION_ID
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.data.repo.Repository
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

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var favouriteFactory: FavouriteViewModelFactory
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var mFusedLocationClient: FusedLocationProviderClient

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
        // Inflate the layout for this fragment
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return inflater.inflate(R.layout.fragment_map, container, false)
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
        requestPermissions(
            arrayOf<String>(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
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
                //getLastLocation()
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
            var latlang = LatLng(mlastLocation?.latitude ?: 0.0, mlastLocation?.longitude ?: 0.0)
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
                var myAddress = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.get(0)?.locality.toString()
                if (myAddress == "null"){
                    myAddress =
                        geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.get(0)?.subAdminArea.toString()
                }else if (myAddress =="null"){
                    geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.get(0)?.adminArea.toString()
                }
                var favouritePlace = FavouritePlace(
                    latLng.latitude,
                    latLng.longitude,
                    myAddress
                )
                checkSaveToFavorite(favouritePlace, myAddress)

            }
        }
    }

    fun checkSaveToFavorite(favouritePlace: FavouritePlace, placeName: String) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alert.setTitle(getString(R.string.favourite))
        alert.setMessage("${getString(R.string.map_saving)} ${placeName} ${getString(R.string.on_fav)}")
        alert.setPositiveButton(getString(R.string.save)) { _: DialogInterface, _: Int ->
            val repository = Repository.getInstance(requireActivity().application)
            favouriteFactory = FavouriteViewModelFactory(repository)
            favouriteViewModel = ViewModelProvider(this, favouriteFactory).get(FavouriteViewModel::class.java)
            favouriteViewModel.insertFavouritePlace(favouritePlace)
            Toast.makeText(requireContext(), " Data has been saved", Toast.LENGTH_SHORT).show()
        }
        alert.setNegativeButton(getString(R.string.no))  { dialog, whichButton ->
            dialog.dismiss()
        }
        val dialog = alert.create()
        dialog.show()
    }

    override fun onMapReady(map: GoogleMap) {
        getLastLocation()
        mMap = map
    }
}