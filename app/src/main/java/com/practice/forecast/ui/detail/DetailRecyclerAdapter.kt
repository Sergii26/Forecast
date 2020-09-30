package com.practice.forecast.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.forecast.R
import com.practice.forecast.databinding.WeatherItemBinding
import com.practice.forecast.ui.detail.DetailRecyclerAdapter.DetailViewHolder
import com.practice.weathermodel.pojo.City
import java.util.*

class DetailRecyclerAdapter : RecyclerView.Adapter<DetailViewHolder>() {
    private val weatherList: MutableList<City?> = ArrayList<City?>()
    fun getWeatherList(): List<City?> {
        return weatherList
    }

    fun setCities(weatherList: List<City?>?) {
        this.weatherList.clear()
        this.weatherList.addAll(weatherList!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: WeatherItemBinding = DataBindingUtil.inflate(inflater, R.layout.weather_item, parent, false)
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bindView(weatherList[position])
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    inner class DetailViewHolder(var binding: WeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(city: City?) {
            binding.city = city
            binding.executePendingBindings()
        }
    }
}