package com.practice.forecast.ui;

import android.os.Bundle;

import com.practice.forecast.R;
import com.practice.forecast.ui.arch.HostActivity;
import com.practice.forecast.ui.detail.DetailContract;
import com.practice.forecast.ui.detail.DetailFragment;
import com.practice.forecast.ui.map.MapContract;
import com.practice.forecast.ui.map.MapFragment;
import com.practice.forecast.ui.splash.SplashContract;
import com.practice.forecast.ui.splash.SplashFragment;

public class MainActivity extends HostActivity implements MapContract.Host, DetailContract.Host, SplashContract.Host {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportFragmentManager().getFragments().size() == 0) {
            addFragment(SplashFragment.newInstance());
        }
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    public void showDetailFragment(int cityId, String cityName) {
        replaceFragmentToBackStack(DetailFragment.newInstance(cityId, cityName));
    }

    public void showMapFragment() {
        replaceFragment(MapFragment.newInstance(), false, null);
    }

}