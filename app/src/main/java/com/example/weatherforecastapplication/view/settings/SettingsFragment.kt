package com.example.weatherforecastapplication.view.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding
import com.example.weatherforecastapplication.model.LocaleManager
import com.example.weatherforecastapplication.view.home.HomeFactoryViewModel
import com.example.weatherforecastapplication.view.home.HomeViewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var unitsShared: SharedPreferences
    lateinit var language: String
    lateinit var unit: String
    lateinit var locationShared: SharedPreferences
    lateinit var location :String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        languageSharedPreferences =
            requireContext().getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        locationShared =
            requireContext().getSharedPreferences(Utility.LOCATION_KEY, Context.MODE_PRIVATE)
        unitsShared = requireContext().getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
        unit = unitsShared.getString(Utility.TEMP_KEY, "metric")!!
        location = locationShared.getString(Utility.LOCATION_KEY, Utility.GPS)!!


        checkLastSettings()
        binding.languageGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when (checkedButtonId) {
                binding.englishButton.id -> {

                    Utility.saveLanguageToSharedPref(
                        requireContext(),
                        Utility.Language_Key,
                        Utility.Language_EN_Value
                    )
                    LocaleManager.setLocale(requireContext())
                    refreshFragment()
                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_settings_to_nav_home)
                }
                binding.arabicButton.id -> {
                    Utility.saveLanguageToSharedPref(
                        requireContext(),
                        Utility.Language_Key,
                        Utility.Language_AR_Value
                    )
                    LocaleManager.setLocale(requireContext())
                    refreshFragment()
                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_settings_to_nav_home)

                }
            }
        }
        binding.temperatureGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when (checkedButtonId) {
                binding.celsuisButton.id -> {
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.METRIC)
                    refreshFragment()
                }
                binding.kelvinButton.id -> {
                    Utility.saveTempToSharedPref(
                        requireContext(),
                        Utility.TEMP_KEY,
                        Utility.STANDARD
                    )
                    refreshFragment()
                }
                binding.fahrenhiteButton.id -> {
                    Utility.saveTempToSharedPref(
                        requireContext(),
                        Utility.TEMP_KEY,
                        Utility.IMPERIAL
                    )
                    refreshFragment()
                }
            }
        }
        binding.locationGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when (checkedButtonId) {
                binding.gpsButton.id -> {
                    Utility.saveLocationSharedPref(
                        requireContext(),
                        Utility.LOCATION_KEY,
                        Utility.GPS
                    )
                }
                binding.mapButton.id -> {
                    Utility.saveLocationSharedPref(
                        requireContext(),
                        Utility.LOCATION_KEY,
                        Utility.MAP
                    )
                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_settings_to_settingMapFragment)

                }
            }
        }

        return root
    }

    private fun checkLastSettings() {
        if (language == Utility.Language_AR_Value) {
            binding.arabicButton.isChecked = true
            binding.englishButton.isChecked = false
        }
        else {
            binding.englishButton.isChecked = true
            binding.arabicButton.isChecked = false
        }
        if (unit == Utility.METRIC) {
            binding.celsuisButton.isChecked = true
            binding.fahrenhiteButton.isChecked = false
            binding.kelvinButton.isChecked = false
        }
        else if (unit == Utility.IMPERIAL) {
            binding.celsuisButton.isChecked = false
            binding.fahrenhiteButton.isChecked = true
            binding.kelvinButton.isChecked = false
        }
        else if(unit == Utility.STANDARD){
            binding.celsuisButton.isChecked = false
            binding.fahrenhiteButton.isChecked = false
            binding.kelvinButton.isChecked = true
        }
        if (location == Utility.MAP) {
            binding.mapButton.isChecked = true
            binding.gpsButton.isChecked = false
        }
        else {
            binding.gpsButton.isChecked = true
            binding.mapButton.isChecked = false
        }

    }

    private fun refreshFragment() {
//        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        requireActivity().recreate()
    }
}