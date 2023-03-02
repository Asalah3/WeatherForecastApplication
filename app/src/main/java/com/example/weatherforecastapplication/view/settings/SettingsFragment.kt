package com.example.weatherforecastapplication.view.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding
import com.example.weatherforecastapplication.model.LocaleManager
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.view.home.HomeFactoryViewModel
import com.example.weatherforecastapplication.view.home.HomeViewModel

class SettingsFragment : Fragment() {
    lateinit var homeFactory: HomeFactoryViewModel
    lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.STANDARD)
                    refreshFragment()
                }
                binding.fahrenhiteButton.id -> {
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.IMPERIAL)
                    refreshFragment()
                }
            }
        }
        return root
    }
    private fun refreshFragment() {
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        homeFactory = HomeFactoryViewModel(Repository(requireContext()))
        homeViewModel =
            ViewModelProvider(requireActivity(), homeFactory)[HomeViewModel::class.java]
        homeViewModel.getCurrentWeather()
    }
    private fun reCall(){

    }
}