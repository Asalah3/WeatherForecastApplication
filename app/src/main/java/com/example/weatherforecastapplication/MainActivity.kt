package com.example.weatherforecastapplication

import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.data.model.LocaleManager
import com.example.weatherforecastapplication.data.repo.Repository
import com.example.weatherforecastapplication.databinding.ActivityMainBinding
import com.example.weatherforecastapplication.view.alerts.AlertViewModelFactory
import com.example.weatherforecastapplication.view.alerts.AlertsViewModel
import com.google.android.material.navigation.NavigationView

const val PERMISSION_ID = 44
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var alertFactory: AlertViewModelFactory
    lateinit var alertsViewModel: AlertsViewModel
    lateinit var language: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = Repository.getInstance(this.application)
        alertFactory = AlertViewModelFactory(repository)

        alertsViewModel =
            ViewModelProvider(this , alertFactory )[AlertsViewModel::class.java]

        languageSharedPreferences =
            this.getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
        if(language == Utility.Language_EN_Value){
            LocaleManager.setLocale(this)
        }else{
            LocaleManager.setLocale(this)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val isNight: Boolean
        val cal: Calendar = Calendar.getInstance()
        val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
        isNight = hour < 6 || hour > 18
        /*if (isNight){
            binding.appBarMain.appBarMain.background = getResources().getDrawable(R.drawable.sky_screen)
        }else{
            binding.appBarMain.appBarMain.background = getResources().getDrawable(R.drawable.backgroud_sky)
        }*/
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favourite, R.id.nav_settings,R.id.nav_alerts,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}