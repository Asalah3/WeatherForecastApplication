package com.example.weatherforecastapplication.view.alerts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.ui.home.view.Utility
import com.example.weatherforecastapplication.databinding.AlertItemBinding
import com.example.weatherforecastapplication.model.LocalAlert

class AlertAdapter(
    private val alerts: List<LocalAlert>,
    private var deleteAction: (LocalAlert) -> Unit,

    ) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    lateinit var binding: AlertItemBinding

    class ViewHolder(var binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AlertItemBinding.inflate(inflater, parent, false)
        return AlertAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return alerts.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = alerts[position]
        holder.binding.fromDate.text = Utility.timeStampToDate(current.startDate)
        holder.binding.toDate.text = Utility.timeStampToDate(current.endDate)
        holder.binding.countryName.text = current.countryName
        holder.binding.atTime.text = Utility.timeStampToHour(current.time)
        holder.binding.deleteAlert.setOnClickListener {
            deleteAction(current)
        }
    }
}