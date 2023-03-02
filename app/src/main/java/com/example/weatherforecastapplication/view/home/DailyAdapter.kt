package com.example.weatherforecastapplication.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.databinding.DayItemBinding
import com.example.weatherforecastapplication.model.Daily

class DailyAdapter (
    private val daily: List<Daily>
    ) : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    lateinit var binding: DayItemBinding

    class ViewHolder(var binding: DayItemBinding) : RecyclerView.ViewHolder(binding.root)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding=DayItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = daily.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = daily[position]
        holder.binding.dayName.text = Utility.timeStampToDay(current.dt)
        holder.binding.dayStatus.text = current.weather[0].description
        with(holder){
            holder.binding.dayImage.setImageResource(Utility.getWeatherIcon(daily[position].weather[0].icon))
        }
        holder.binding.dayTemp.text = "${current.temp.min.toInt()} / ${current.temp.max.toInt()} °C"

    }
}
