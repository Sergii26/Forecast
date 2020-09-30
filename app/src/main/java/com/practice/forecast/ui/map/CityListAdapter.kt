package com.practice.forecast.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.forecast.R
import com.practice.forecast.databinding.CityItemBinding
import com.practice.forecast.ui.map.CityListAdapter.CityViewHolder
import com.practice.weathermodel.pojo.City
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

class CityListAdapter : RecyclerView.Adapter<CityViewHolder>() {
    private val cities: MutableList<City?> = ArrayList()
    private val itemViewClickSubject = PublishSubject.create<City?>()
    private val handler: CityHandler = object : CityHandler() {
        override fun onCityClick(city: City?) {
            itemViewClickSubject.onNext(city!!)
        }
    }
    val clicksObservable: Observable<City?>
        get() = itemViewClickSubject

    fun getCities(): List<City?> {
        return cities
    }

    fun setCities(cities: List<City?>?) {
        this.cities.clear()
        this.cities.addAll(cities!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: CityItemBinding = DataBindingUtil.inflate(inflater, R.layout.city_item, parent, false)
        binding.handler = handler
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bindView(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    inner class CityViewHolder(var binding: CityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(city: City?) {
            binding.city = city
            binding.executePendingBindings()
        }
    }
}