package com.example.weatherforecastapplication.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.databinding.TempItemBinding
import com.example.weatherforecastapplication.model.Current

class HourlyAdapter (private val hourly: List<Current>
) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    lateinit var binding: TempItemBinding

    class ViewHolder(var binding: TempItemBinding) : RecyclerView.ViewHolder(binding.root)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= TempItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = hourly.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = hourly[position]
        with(holder){
            holder.binding.hourImage.setImageResource(Utility.getWeatherIcon(hourly[position].weather[0].icon))
        }
        holder.binding.temp.text = "${current.temp.toInt()} °C"
        holder.binding.hour.text = Utility.timeStampToHour(current.dt)

    }
}