package com.practice.forecast.ui.map;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.practice.forecast.R;
import com.practice.forecast.databinding.CityItemBinding;
import com.practice.weathermodel.pojo.City;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityViewHolder> {

    private final List<City> cities = new ArrayList<>();
    private final CityHandler handler;

    public CityListAdapter(CityHandler handler) {
        this.handler = handler;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities.clear();
        this.cities.addAll(cities);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CityItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.city_item, parent, false);
        binding.setHandler(handler);
        return new CityViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.bindView(cities.get(position));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder{

        CityItemBinding binding;

        public CityViewHolder(@NonNull CityItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(City city){
            binding.setCity(city);
            binding.executePendingBindings();;
        }
    }
}
