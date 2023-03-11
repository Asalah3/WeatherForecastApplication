package com.example.weatherforecastapplication.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.databinding.DayItemBinding
import com.example.weatherforecastapplication.data.model.Daily
import com.squareup.picasso.Picasso

class DailyAdapter (
    private val daily: List<Daily>, private val unit: String, private val context: Context
    ) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    lateinit var binding: DayItemBinding
    lateinit var languageSharedPreferences: SharedPreferences
    lateinit var language: String

    class ViewHolder(var binding: DayItemBinding) : RecyclerView.ViewHolder(binding.root)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        languageSharedPreferences =
            context.getSharedPreferences(Utility.Language_Value_Key, Context.MODE_PRIVATE)
        language = languageSharedPreferences.getString(Utility.Language_Key, "en")!!
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding=DayItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = daily.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = daily[position]
        holder.binding.dayName.text = Utility.timeStampToDay(current.dt)
        holder.binding.dayStatus.text = current.weather[0].description
        Picasso.get().load("https://openweathermap.org/img/wn/${daily[position].weather[0].icon}@4x.png").into(holder.binding.dayImage)
        if (language == Utility.Language_AR_Value)
        holder.binding.dayTemp.text = "${Utility.convertNumbersToArabic(current.temp.min.toInt())} / ${Utility.convertNumbersToArabic(current.temp.max.toInt())} $unit"
        else
            holder.binding.dayTemp.text = "${current.temp.min.toInt()} / ${current.temp.max.toInt()} $unit"
    }
}
