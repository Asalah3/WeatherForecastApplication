package com.example.weatherforecastapplication.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.PERMISSION_ID
import com.example.weatherforecastapplication.data.repo.Repository
import com.example.weatherforecastapplication.data.network.ApiState
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.roundToInt

class HomeFragment(
) : Fragment() {
    lateinit var dailyAdapter: DailyAdapter
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var homeFactory: HomeFactoryViewModel
    lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var latLng: LatLng
    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var unit: String
    lateinit var unitsShared: SharedPreferences
    lateinit var language: String
    lateinit var myAddress: String
    lateinit var latitudeSharedPreferences: SharedPreferences
    lateinit var longitudeSharedPreferences: SharedPreferences
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var locationShared: SharedPreferences
    lateinit var location: String

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val repository = Repository.getInstance(requireActivity().application)
        homeFactory = HomeFactoryViewModel(repository)
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
//        getLastLocation()
        homeViewModel.getCurrentWeather(LatLng(30.6136, 32.2836))

//        homeViewModel.getCurrentWeather(LatLng(latLng.latitude, latLng.longitude))
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Toast.makeText(requireContext(), "on view created", Toast.LENGTH_SHORT).show()
        super.onViewCreated(view, savedInstanceState)
        languageSharedPreferences =
            requireContext().getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        unitsShared = requireContext().getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
        unit = unitsShared.getString(Utility.TEMP_KEY, "metric")!!

        latitudeSharedPreferences =
            requireContext().getSharedPreferences(Utility.LATITUDE_KEY, Context.MODE_PRIVATE)
        longitudeSharedPreferences =
            requireContext().getSharedPreferences(Utility.LONGITUDE_KEY, Context.MODE_PRIVATE)
        latitude = latitudeSharedPreferences.getString(Utility.LATITUDE_KEY, "0.0")!!.toDouble()
        longitude = longitudeSharedPreferences.getString(Utility.LONGITUDE_KEY, "0.0")!!.toDouble()
        locationShared =
            requireContext().getSharedPreferences(Utility.LOCATION_KEY, Context.MODE_PRIVATE)
        location = locationShared.getString(Utility.LOCATION_KEY, Utility.GPS)!!
        lifecycleScope.launch {
            homeViewModel.root.collectLatest { result ->
                when (result) {
                    is ApiState.Success -> {
                        println(result.data)
                        binding.pBar.visibility = View.GONE
                        binding.homescrollView.visibility = View.VISIBLE
                        if (result.data.current!!.weather[0].icon == "04d" || result.data.current.weather[0].icon == "04n")
                            binding.gifHome.visibility = View.VISIBLE
                        else
                            binding.gifHome.visibility = View.GONE

                        binding.currentStatus.text = result.data.current!!.weather[0].description

                        try {
                            if (location == Utility.GPS) {
                                val geoCoder = Geocoder(requireContext())
                                myAddress =
                                    geoCoder.getFromLocation(
                                        30.6136, 32.2836,
                                        1
                                    )?.get(0)?.locality
                                        .toString()
                            } else if (location == Utility.MAP) {
                                val geoCoder = Geocoder(requireContext())
                                myAddress =
                                    geoCoder.getFromLocation(
                                        latitude, longitude,
                                        1
                                    )?.get(0)?.locality
                                        .toString()
                            }
                            binding.currentLocation.text = "${myAddress}"
                        } catch (e: Exception) {
                            binding.currentLocation.text = "${result.data.timezone}"
                        }
                        Picasso.get()
                            .load("https://openweathermap.org/img/wn/${result.data.current.weather[0].icon}@4x.png")
                            .into(binding.currentStatusImage)
                        println(result.data.current)
                        val current = LocalDateTime.now()
                        val arabicFormatter =
                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                .withDecimalStyle(DecimalStyle.of(Locale("ar")))
                        val englishFormatter =
                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                .withDecimalStyle(DecimalStyle.of(Locale("en")))
                        if (language == Utility.Language_AR_Value) {
                            binding.currentDate.text = " ${current.format(arabicFormatter)}"
                        } else {
                            binding.currentDate.text = " ${current.format(englishFormatter)}"
                        }

                        if (language == Utility.Language_EN_Value && unit == Utility.METRIC) {
                            hourlyAdapter =
                                HourlyAdapter(result.data.hourly, "°C", requireContext())
                            dailyAdapter = DailyAdapter(result.data.daily, "°C", requireContext())
                            binding.currentTemp.text = "${result.data.current.temp.roundToInt()} °C"
                            binding.currentFeelsLike.text =
                                "${result.data.current.temp.roundToInt()} °C"
                            binding.pressureMeasure.text = "${result.data.current.pressure} hpa"
                            binding.humidityMeasure.text = "${result.data.current.humidity} %"
                            binding.windMeasure.text = "${result.data.current.windSpeed} m/s"
                            binding.cloudMeasure.text = "${result.data.current.clouds} %"
                            binding.violateMeasure.text = "${result.data.current.uvi}"
                            binding.visibilityMeasure.text = "${result.data.current.visibility} m"

                        } else if (language == Utility.Language_EN_Value && unit == Utility.IMPERIAL) {
                            hourlyAdapter =
                                HourlyAdapter(result.data.hourly, "°F", requireContext())
                            dailyAdapter = DailyAdapter(result.data.daily, "°F", requireContext())
                            binding.currentTemp.text = "${result.data.current.temp.roundToInt()} °F"
                            binding.currentFeelsLike.text =
                                "${result.data.current.temp.roundToInt()} °F"
                            binding.pressureMeasure.text = "${result.data.current.pressure} hpa"
                            binding.humidityMeasure.text = "${result.data.current.humidity} %"
                            binding.windMeasure.text = "${result.data.current.windSpeed} m/s"
                            binding.cloudMeasure.text = "${result.data.current.clouds} %"
                            binding.violateMeasure.text = "${result.data.current.uvi}"
                            binding.visibilityMeasure.text = "${result.data.current.visibility} m"

                        } else if (language == Utility.Language_EN_Value && unit == Utility.STANDARD) {
                            dailyAdapter = DailyAdapter(result.data.daily, "°K", requireContext())
                            hourlyAdapter =
                                HourlyAdapter(result.data.hourly, "°K", requireContext())
                            binding.currentTemp.text = "${result.data.current.temp.roundToInt()} °K"
                            binding.currentFeelsLike.text =
                                "${result.data.current.temp.roundToInt()} °K"
                            binding.pressureMeasure.text = "${result.data.current.pressure} hpa"
                            binding.humidityMeasure.text = "${result.data.current.humidity} %"
                            binding.windMeasure.text = "${result.data.current.windSpeed} m/s"
                            binding.cloudMeasure.text = "${result.data.current.clouds} %"
                            binding.violateMeasure.text = "${result.data.current.uvi}"
                            binding.visibilityMeasure.text = "${result.data.current.visibility} m"
                        } else if (language == Utility.Language_AR_Value && unit == Utility.METRIC) {
                            hourlyAdapter =
                                HourlyAdapter(result.data.hourly, " س° ", requireContext())
                            dailyAdapter = DailyAdapter(result.data.daily, " س° ", requireContext())
                            binding.currentTemp.text =
                                " ${Utility.convertNumbersToArabic(result.data.current.temp.roundToInt())}  س° "
                            binding.currentFeelsLike.text =
                                " ${Utility.convertNumbersToArabic(result.data.current.temp.roundToInt())}  س° "
                            binding.pressureMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.pressure)} بار"
                            binding.humidityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.humidity)} %"
                            binding.windMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.windSpeed)} م/ث"
                            binding.cloudMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.clouds)} %"
                            binding.violateMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.uvi)}"
                            binding.visibilityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.visibility)} م"

                        } else if (language == Utility.Language_AR_Value && unit == Utility.IMPERIAL) {
                            hourlyAdapter =
                                HourlyAdapter(result.data.hourly, " ف° ", requireContext())
                            dailyAdapter = DailyAdapter(result.data.daily, " ف° ", requireContext())
                            binding.currentTemp.text =
                                " ${Utility.convertNumbersToArabic(result.data.current.temp.roundToInt())}  ف° "
                            binding.currentFeelsLike.text =
                                " ${Utility.convertNumbersToArabic(result.data.current.feelsLike.roundToInt())} ف° "
                            binding.pressureMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.pressure)} بار"
                            binding.humidityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.humidity)} %"
                            binding.windMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.windSpeed)} م/ث"
                            binding.cloudMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.clouds)} %"
                            binding.violateMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.uvi)}"
                            binding.visibilityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.visibility)} م"

                        } else if (language == Utility.Language_AR_Value && unit == Utility.STANDARD) {
                            dailyAdapter = DailyAdapter(result.data.daily, " ك° ", requireContext())
                            hourlyAdapter =
                                HourlyAdapter(result.data.hourly, " ك° ", requireContext())
                            binding.currentTemp.text =
                                " ${Utility.convertNumbersToArabic(result.data.current.temp.roundToInt())}  ك° "
                            binding.currentFeelsLike.text =
                                " ${Utility.convertNumbersToArabic(result.data.current.feelsLike.roundToInt())} ك° "
                            binding.pressureMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.pressure)} بار"
                            binding.humidityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.humidity)} %"
                            binding.windMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.windSpeed)} م/ث"
                            binding.cloudMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.clouds)} %"
                            binding.violateMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.uvi)}"
                            binding.visibilityMeasure.text =
                                "${Utility.convertNumbersToArabic(result.data.current.visibility)} م"
                        }
                        binding.recyclerViewTemp.adapter = hourlyAdapter
                        binding.recyclerViewHome.adapter = dailyAdapter
                    }
                    is ApiState.Failure -> {
                        binding.pBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "${result.msg}", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        binding.pBar.visibility = View.VISIBLE
                        binding.homescrollView.visibility = View.GONE
                    }
                }
            }
        }
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
        mLocationRequest.interval = 0
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )

    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        @SuppressLint("SuspiciousIndentation")
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val mlastLocation: Location? = p0.lastLocation
            latLng = LatLng(mlastLocation?.latitude ?: 0.0, mlastLocation?.longitude ?: 0.0)
            homeViewModel.getCurrentWeather(latLng)
            homeViewModel.getCurrentWeather(LatLng(32.0, 33.0))
//            mFusedLocationClient.removeLocationUpdates(this)
        }
    }
}


