package com.example.weatherforecastapplication.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.model.LocaleManager
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.ApiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment(
) : Fragment() {
    lateinit var dailyAdapter: DailyAdapter
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var homeFactory: HomeFactoryViewModel
    lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        homeFactory = HomeFactoryViewModel(Repository(requireContext()))
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var languageSharedPreferences =
            requireContext().getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        var language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!

        lifecycleScope.launch {
/*
            if (language == "ar") {
                LocaleManager.setLocale(requireContext())
            }
*/
//            homeViewModel.getCurrentWeather()
                homeViewModel.root.collectLatest { result ->
                when (result) {
                    is ApiState.Success -> {
                        println(result.data)
                        binding.pBar.visibility = View.GONE
                        binding.homescrollView.visibility = View.VISIBLE
                        dailyAdapter = DailyAdapter(result.data.daily)
                        binding.recyclerViewHome.adapter = dailyAdapter
                        binding.currentStatus.text = result.data.current.weather[0].description
                        binding.currentTemp.text = "${result.data.current.temp} °C"
                        binding.currentLocation.text = "${result.data.timezone}"
                        binding.currentStatusImage.setImageResource(Utility.getWeatherIcon(result.data.current.weather[0].icon))
                        binding.pressureMeasure.text = "${result.data.current.pressure} hpa"
                        binding.humidityMeasure.text = "${result.data.current.humidity} %"
                        binding.windMeasure.text = "${result.data.current.windSpeed} m/s"
                        binding.cloudMeasure.text = "${result.data.current.clouds} %"
                        binding.violateMeasure.text = result.data.current.uvi.toString()
                        binding.visibilityMeasure.text = "${result.data.current.visibility} m"

                        hourlyAdapter = HourlyAdapter(result.data.hourly)
                        binding.recyclerViewTemp.adapter = hourlyAdapter
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

}