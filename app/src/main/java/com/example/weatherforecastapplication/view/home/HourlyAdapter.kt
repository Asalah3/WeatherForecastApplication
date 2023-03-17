package com.example.weatherforecastapplication.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.data.model.Current
import com.example.weatherforecastapplication.databinding.TempItemBinding
import com.squareup.picasso.Picasso

class HourlyAdapter(
    private val hourly: List<Current>, private val unit: String, private val context: Context
) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    lateinit var binding: TempItemBinding
    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var language: String

    class ViewHolder(var binding: TempItemBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        languageSharedPreferences =
            context.getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!

        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = TempItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = hourly.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = hourly[position]
        with(holder){
            holder.binding.hourImage.setImageResource(Utility.getWeatherIcon(hourly[position].weather[0].icon))
        }
//        Picasso.get().load("https://openweathermap.org/img/wn/${hourly[position].weather[0].icon}@4x.png").into(holder.binding.hourImage)
        if (language == Utility.Language_AR_Value)
        holder.binding.temp.text =
            "${Utility.convertNumbersToArabic(current.temp.toInt())} $unit"
        else
            holder.binding.temp.text =
                "${current.temp.toInt()} $unit"
        holder.binding.hour.text = Utility.timeStampToHour(current.dt)

    }
}