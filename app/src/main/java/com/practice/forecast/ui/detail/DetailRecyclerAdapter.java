package com.practice.forecast.ui.detail;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.practice.forecast.R;
import com.practice.forecast.databinding.WeatherItemBinding;
import com.practice.weathermodel.pojo.City;
import com.practice.weathermodel.pojo.Main;
import com.practice.weathermodel.pojo.Rain;
import com.practice.weathermodel.pojo.Weather;
import com.practice.weathermodel.pojo.Wind;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class DetailRecyclerAdapter extends RecyclerView.Adapter<DetailRecyclerAdapter.DetailViewHolder>{

    private final List<City> weatherList = new ArrayList();

    public List<City> getWeatherList() {
        return weatherList;
    }

    public void setCities(List<City> weatherList) {
        this.weatherList.clear();
        this.weatherList.addAll(weatherList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        WeatherItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.weather_item, parent, false);
        return new DetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.bindView(weatherList.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


    public class DetailViewHolder extends RecyclerView.ViewHolder{

        WeatherItemBinding binding;

        public DetailViewHolder(@NonNull WeatherItemBinding binding) {

            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(City city){
            binding.setCity(city);
            binding.executePendingBindings();
        }
    }
}
