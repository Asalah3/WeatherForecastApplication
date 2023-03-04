package com.example.weatherforecastapplication.view.favourite

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.databinding.FragmentFavouritePlaceBinding
import com.example.weatherforecastapplication.model.FavouritePlace
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.ApiState
import com.example.weatherforecastapplication.view.home.DailyAdapter
import com.example.weatherforecastapplication.view.home.HourlyAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouritePlaceFragment : Fragment() {
    lateinit var favouriteFactory: FavouriteViewModelFactory
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var dailyAdapter: DailyAdapter
    lateinit var hourlyAdapter: HourlyAdapter
    private var _binding: FragmentFavouritePlaceBinding? = null
    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var unitsShared: SharedPreferences
    lateinit var language: String
    lateinit var unit: String
    private val binding get() = _binding!!
    lateinit var favorite : FavouritePlace

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        languageSharedPreferences =
            requireContext().getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        unitsShared = requireContext().getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
        unit = unitsShared.getString(Utility.TEMP_KEY, "metric")!!
        favouriteFactory = FavouriteViewModelFactory(Repository(requireContext()))

        favouriteViewModel =
            ViewModelProvider(this, favouriteFactory)[FavouriteViewModel::class.java]

        _binding = FragmentFavouritePlaceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        arguments?.let {
            favorite = it.getSerializable("favorite") as FavouritePlace
        }

        favouriteViewModel.getFavouriteWeather(favorite)
        lifecycleScope.launch {
            favouriteViewModel.root.collectLatest { result ->
                when (result) {
                    is ApiState.Success -> {
                        println(result.data)
                        binding.pBar.visibility = View.GONE
                        binding.scrollView.visibility = View.VISIBLE
                        binding.currentStatus.text = result.data.current.weather[0].description
                        binding.currentStatusImage.setImageResource(Utility.getWeatherIcon(result.data.current.weather[0].icon))


                        if (language == Utility.Language_EN_Value && unit == Utility.METRIC) {
                            hourlyAdapter = HourlyAdapter(result.data.hourly, "°C",requireContext())
                            dailyAdapter = DailyAdapter(result.data.daily, "°C",requireContext())
                            binding.currentTemp.text = "${result.data.current.temp} °C"
                            binding.pressureMeasure.text = "${result.data.current.pressure} hpa"
                            binding.humidityMeasure.text = "${result.data.current.humidity} %"
                            binding.windMeasure.text = "${result.data.current.windSpeed} m/s"
                            binding.cloudMeasure.text = "${result.data.current.clouds} %"
                            binding.violateMeasure.text = "${result.data.current.uvi}"
                            binding.visibilityMeasure.text = "${result.data.current.visibility} m"

                        }
                        else if (language == Utility.Language_EN_Value && unit == Utility.IMPERIAL) {
                            hourlyAdapter = HourlyAdapter(result.data.hourly, "°F",requireContext())
                            dailyAdapter = DailyAdapter(result.data.daily, "°F",requireContext())
                            binding.currentTemp.text = "${result.data.current.temp} °F"
                            binding.pressureMeasure.text = "${result.data.current.pressure} hpa"
                            binding.humidityMeasure.text = "${result.data.current.humidity} %"
                            binding.windMeasure.text = "${result.data.current.windSpeed} m/s"
                            binding.cloudMeasure.text = "${result.data.current.clouds} %"
                            binding.violateMeasure.text = "${result.data.current.uvi}"
                            binding.visibilityMeasure.text = "${result.data.current.visibility} m"

                        }
                        else if (language == Utility.Language_EN_Value && unit == Utility.STANDARD) {
                            dailyAdapter = DailyAdapter(result.data.daily, "°K",requireContext())
                            hourlyAdapter = HourlyAdapter(result.data.hourly, "°K",requireContext())
                            binding.currentTemp.text = "${result.data.current.temp} °K"
                            binding.pressureMeasure.text = "${result.data.current.pressure} hpa"
                            binding.humidityMeasure.text = "${result.data.current.humidity} %"
                            binding.windMeasure.text = "${result.data.current.windSpeed} m/s"
                            binding.cloudMeasure.text = "${result.data.current.clouds} %"
                            binding.violateMeasure.text = "${result.data.current.uvi}"
                            binding.visibilityMeasure.text = "${result.data.current.visibility} m"
                        }
                        else if (language == Utility.Language_AR_Value && unit == Utility.METRIC) {
                            hourlyAdapter = HourlyAdapter(result.data.hourly, " س° ",requireContext())
                            dailyAdapter = DailyAdapter(result.data.daily, " س° ",requireContext())
                            binding.currentTemp.text = "${Utility.convertNumbersToArabic(result.data.current.temp)}  س° "
                            binding.pressureMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.pressure)} بار"
                            binding.humidityMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.humidity)} %"
                            binding.windMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.windSpeed)} م/ث"
                            binding.cloudMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.clouds)} %"
                            binding.violateMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.uvi)}"
                            binding.visibilityMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.visibility)} م"

                        }
                        else if (language == Utility.Language_AR_Value && unit == Utility.IMPERIAL) {
                            hourlyAdapter = HourlyAdapter(result.data.hourly, " ف° ",requireContext())
                            dailyAdapter = DailyAdapter(result.data.daily, " ف° ",requireContext())
                            binding.currentTemp.text = "${Utility.convertNumbersToArabic(result.data.current.temp)}  ف° "
                            binding.pressureMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.pressure)} بار"
                            binding.humidityMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.humidity)} %"
                            binding.windMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.windSpeed)} م/ث"
                            binding.cloudMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.clouds)} %"
                            binding.violateMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.uvi)}"
                            binding.visibilityMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.visibility)} م"

                        }
                        else if (language == Utility.Language_AR_Value && unit == Utility.STANDARD) {
                            dailyAdapter = DailyAdapter(result.data.daily, " ك° ",requireContext())
                            hourlyAdapter = HourlyAdapter(result.data.hourly, " ك° ",requireContext())
                            binding.currentTemp.text = "${Utility.convertNumbersToArabic(result.data.current.temp)}  ك° "
                            binding.pressureMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.pressure)} بار"
                            binding.humidityMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.humidity)} %"
                            binding.windMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.windSpeed)} م/ث"
                            binding.cloudMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.clouds)} %"
                            binding.violateMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.uvi)}"
                            binding.visibilityMeasure.text = "${Utility.convertNumbersToArabic(result.data.current.visibility)} م"
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
                        binding.scrollView.visibility = View.GONE
                    }
                }
            }
        }
        return root
    }

}