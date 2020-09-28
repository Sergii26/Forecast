package com.practice.forecast.ui.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.practice.forecast.R;
import com.practice.forecast.ui.arch.mvvm.MvvmFragment;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class SplashFragment extends MvvmFragment<SplashContract.Host> {
    private SplashContract.BaseSplashViewModel viewModel;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSplashFragmentComponent.builder()
                .splashFragmentModule(new SplashFragmentModule())
                .build()
                .injectSplashFragment(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(SplashViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getIsReady().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean && hasCallBack()) {
                getCallBack().openMapFragment();
            }
        });
        viewModel.startTimer();
        final TextView splash = view.findViewById(R.id.tvAppName);
        splash.startAnimation(getAnimation());
    }

    private Animation getAnimation(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.splash_anim);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(700);
        return animation;
    }
}
