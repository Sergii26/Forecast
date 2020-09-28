package com.practice.forecast.ui.detail;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.practice.forecast.R;
import com.practice.forecast.databinding.FragmentDetailBinding;
import com.practice.forecast.ui.arch.mvi.MviFragment;
import com.practice.weathermodel.logger.ILog;
import com.practice.weathermodel.logger.Logger;
import com.practice.weathermodel.pojo.City;

import java.util.List;

import javax.inject.Inject;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public class DetailFragment extends MviFragment<DetailScreenState, DetailContract.Host> implements DetailContract.View  {

    public static final String KEY_CITY_ID = "key_city_id";
    public static final String KEY_CITY_NAME = "city_name";

    private ProgressBar progressLoading;
    private final ILog logger = Logger.withTag("MyLog");
    private DetailContract.BaseDetailViewModel viewModel;
    private FragmentDetailBinding binding;
    private final DetailRecyclerAdapter adapter = new DetailRecyclerAdapter();
    private RecyclerView rvDetail;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if(hasCallBack()) {
                getCallBack().backToMapFragment();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerDetailFragmentComponent.builder()
                .detailFragmentModule(new DetailFragmentModule())
                .build()
                .injectDetailFragment(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(DetailViewModel.class);
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
        progressLoading = view.findViewById(R.id.progress_loading);
        rvDetail = view.findViewById(R.id.rvWeather);
        rvDetail.setAdapter(adapter);
        rvDetail.setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false));
        binding.setCityName(getArguments().getString(KEY_CITY_NAME));
        viewModel.downloadCity(String.valueOf(getArguments().getInt(KEY_CITY_ID)));
        viewModel.getStateHolderObservable().observe(getViewLifecycleOwner(), detailScreenState -> {
            logger.log("DetailFragment onStateChange()");
            detailScreenState.visit(DetailFragment.this);
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void showProgress() {
        progressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setListToAdapter(List<City> weatherLIst) {
        binding.setCity(weatherLIst.get(1));
        adapter.setCities(weatherLIst);
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity(), R.string.detail_error_state, Toast.LENGTH_SHORT).show();
    }
}
