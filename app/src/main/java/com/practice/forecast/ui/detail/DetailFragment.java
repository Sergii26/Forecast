package com.practice.forecast.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.practice.forecast.R;
import com.practice.forecast.databinding.FragmentDetailBinding;
import com.practice.weathermodel.logger.ILog;
import com.practice.weathermodel.logger.Logger;
import com.practice.weathermodel.pojo.City;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public class DetailFragment extends Fragment {

    public static final String KEY_CITY_ID = "key_city_id";
    public static final String KEY_CITY_NAME = "city_name";

    private final ILog logger = Logger.withTag("MyLog");
    private DetailContract.BaseDetailViewModel viewModel;
    private FragmentDetailBinding binding;
    private final DetailRecyclerAdapter adapter = new DetailRecyclerAdapter();
    private RecyclerView rvDetail;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public static DetailFragment newInstance(int cityId, String cityName){
        final Bundle b = new Bundle();
        b.putInt(KEY_CITY_ID, cityId);
        b.putString(KEY_CITY_NAME, cityName);
        final DetailFragment f = new DetailFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerDetailFragmentComponent.builder()
                .detailFragmentModule(new DetailFragmentModule())
                .build()
                .injectDetailFragment(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDetail = view.findViewById(R.id.rvWeather);
        rvDetail.setAdapter(adapter);
        rvDetail.setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false));
        viewModel = new ViewModelProvider(this, viewModelFactory).get(DetailViewModel.class);
        viewModel.downloadCity(String.valueOf(getArguments().getInt(KEY_CITY_ID)));
        binding.setCityName(getArguments().getString(KEY_CITY_NAME));
        viewModel.getCityObservable().observe(getViewLifecycleOwner(), new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cities) {
                logger.log("DetailFragment onCities change");
                binding.setCity(cities.get(0));
                adapter.setCities(cities);
            }
        });
    }

}
