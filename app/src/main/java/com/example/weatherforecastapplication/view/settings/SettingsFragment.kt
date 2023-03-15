package com.example.weatherforecastapplication.view.settings

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.example.weatherforecastapplication.data.model.LocaleManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var unitsShared: SharedPreferences
    lateinit var language: String
    lateinit var unit: String
    lateinit var locationShared: SharedPreferences
    lateinit var location: String
    lateinit var alertSharedPreferences: SharedPreferences
    lateinit var alertString: String
    lateinit var modeSharedPreferences: SharedPreferences
    lateinit var mode: String

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
        alertSharedPreferences =
            requireContext().getSharedPreferences(Utility.ALERT, Context.MODE_PRIVATE)
        alertString = alertSharedPreferences.getString(Utility.ALERT, Utility.NOTIFICATION)!!
        modeSharedPreferences =
            requireContext().getSharedPreferences(Utility.MODE, Context.MODE_PRIVATE)
        mode = modeSharedPreferences.getString(Utility.MODE, Utility.LIGHTMODE)!!


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
        /*binding.modeGroup?.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when (checkedButtonId) {
                binding.lightModeButton?.id -> {
                    Utility.saveModeSharedPref(
                        requireContext(),
                        Utility.MODE,
                        Utility.LIGHTMODE
                    )
                }
                binding.darkModeButton?.id -> {
                    Utility.saveModeSharedPref(
                        requireContext(),
                        Utility.MODE,
                        Utility.DARKMODE
                    )
                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_settings_to_settingMapFragment)

                }
            }
        }*/
        binding.alertGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when (checkedButtonId) {
                binding.alarmButton.id -> {
                    checkPermissionOfOverlay()
                    if (Settings.canDrawOverlays(requireContext())) {
                        Utility.saveAlertToSharedPref(
                            requireContext(),
                            Utility.ALERT,
                            Utility.ALARM
                        )
                    }
                }
                binding.notificationButton.id -> {

                    Utility.saveAlertToSharedPref(
                        requireContext(),
                        Utility.ALERT,
                        Utility.NOTIFICATION
                    )


                }
            }
        }
        return root
    }

    private fun checkLastSettings() {
        if (language == Utility.Language_AR_Value) {
            binding.arabicButton.isChecked = true
            binding.englishButton.isChecked = false
        } else {
            binding.englishButton.isChecked = true
            binding.arabicButton.isChecked = false
        }
        if (unit == Utility.METRIC) {
            binding.celsuisButton.isChecked = true
            binding.fahrenhiteButton.isChecked = false
            binding.kelvinButton.isChecked = false
        } else if (unit == Utility.IMPERIAL) {
            binding.celsuisButton.isChecked = false
            binding.fahrenhiteButton.isChecked = true
            binding.kelvinButton.isChecked = false
        } else if (unit == Utility.STANDARD) {
            binding.celsuisButton.isChecked = false
            binding.fahrenhiteButton.isChecked = false
            binding.kelvinButton.isChecked = true
        }
        if (location == Utility.MAP) {
            binding.mapButton.isChecked = true
            binding.gpsButton.isChecked = false
        } else {
            binding.gpsButton.isChecked = true
            binding.mapButton.isChecked = false
        }
        if (alertString == Utility.NOTIFICATION) {
            binding.notificationButton?.isChecked = true
            binding.alarmButton?.isChecked = false
        } else {
            binding.alarmButton?.isChecked = true
            binding.notificationButton?.isChecked = false
        }
        /*if (mode == Utility.LIGHTMODE) {
            binding.lightModeButton?.isChecked = true
            binding.darkModeButton?.isChecked = false
        } else {
            binding.darkModeButton?.isChecked = true
            binding.lightModeButton?.isChecked = false
        }*/
    }

    private fun refreshFragment() {
        requireActivity().recreate()
    }

    private fun checkPermissionOfOverlay() {
        // Check if we already  have permission
        if (!Settings.canDrawOverlays(requireContext())) {

            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle(getString(R.string.on_top))
                .setMessage(getString(R.string.alarm_permission))
                .setPositiveButton(getString(R.string.okey)) { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)
                    )
                    startActivityForResult(intent, 1)
                    dialog.dismiss()

                }.setNegativeButton(getString(R.string.no)) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }
}