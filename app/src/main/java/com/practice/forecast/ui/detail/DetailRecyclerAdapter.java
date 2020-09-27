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
        City city = new City();
        city.setDt(0);
        Main main = new Main();
        Weather weather = new Weather();
        weather.setIcon("label");
        Wind wind = new Wind();
        wind.setSpeed(-1);
        List<Weather> weatherListForCity = new ArrayList<>();
        weatherListForCity.add(0, weather);
        city.setWeather(weatherListForCity);
        city.setWind(wind);
        main.setTemp(-100);
        main.setPressure(-1);
        main.setHumidity(-1);
        Rain rain = new Rain();
        rain.set3h(-1);
        city.setRain(rain);
        city.setMain(main);
        weatherList.add(0, city);
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
