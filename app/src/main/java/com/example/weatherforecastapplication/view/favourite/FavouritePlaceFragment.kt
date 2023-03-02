package com.example.weatherforecastapplication.view.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favouriteFactory = FavouriteViewModelFactory(Repository(requireContext()))

        favouriteViewModel =
            ViewModelProvider(this, favouriteFactory).get(FavouriteViewModel::class.java)

        _binding = FragmentFavouritePlaceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        arguments?.let {
            var favorite = it.getSerializable("favorite")
            println("Place ${favorite}")
            favouriteViewModel.getFavouriteWeather(favorite as FavouritePlace)
        }


        lifecycleScope.launch {
            favouriteViewModel.root.collectLatest { result ->
                when (result) {
                    is ApiState.Success -> {
                        binding.pBar.visibility = View.GONE
                        binding.scrollView.visibility = View.VISIBLE
                        dailyAdapter = DailyAdapter(result.data.daily)
                        binding.recyclerViewHome.adapter = dailyAdapter
                        binding.currentStatus.text = result.data.current.weather[0].description
                        binding.currentTemp.text = "${result.data.current.temp} °C"
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
                        binding.scrollView.visibility = View.GONE
                    }
                }
            }
        }
        return root
    }

}