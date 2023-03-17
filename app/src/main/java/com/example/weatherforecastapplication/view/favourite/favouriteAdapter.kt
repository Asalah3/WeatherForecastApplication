package com.example.weatherforecastapplication.view.favourite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.data.model.FavouritePlace
import com.example.weatherforecastapplication.databinding.FavouriteItemBinding

class FavouriteAdapter (private val favouritePlace: List<FavouritePlace>,
                        private var action : (FavouritePlace)->Unit,
                        private var deleteAction : (FavouritePlace)->Unit,

                        ) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    lateinit var binding: FavouriteItemBinding

    class ViewHolder(var binding: FavouriteItemBinding) : RecyclerView.ViewHolder(binding.root)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= FavouriteItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favouritePlace.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = favouritePlace[position]
        holder.binding.favouriteCountryName.text = current.countryName
        holder.binding.favouriteCardView.setOnClickListener {
            action(current)
        }
        holder.binding.deleteFavourite.setOnClickListener {
            deleteAction(current)
        }
    }
}