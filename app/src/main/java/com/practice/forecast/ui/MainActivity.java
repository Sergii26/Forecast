package com.practice.forecast.ui;

import android.os.Bundle;

import com.practice.forecast.R;
import com.practice.forecast.ui.detail.DetailContract;
import com.practice.forecast.ui.detail.DetailFragment;
import com.practice.forecast.ui.map.MapContract;
import com.practice.forecast.ui.splash.SplashContract;
import com.practice.weathermodel.logger.ILog;
import com.practice.weathermodel.logger.Logger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements SplashContract.Host, MapContract.Host, DetailContract.Host {
    private final ILog logger = Logger.withTag("MyLog");
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if initialization in OnPostCreate method navController == null
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    }

    @Override
    public void backToMapFragment() {
        navController.navigate(R.id.action_detailFragment_to_mapFragment);
    }

    @Override
    public void openDetailFragment(String cityName, int cityId) {
        Bundle args = new Bundle();
        args.putString(DetailFragment.KEY_CITY_NAME, cityName);
        args.putInt(DetailFragment.KEY_CITY_ID, cityId);
        navController.navigate(R.id.action_mapFragment_to_detailFragment, args);
    }

    @Override
    public void closeApp() {
        finish();
    }

    @Override
    public void openMapFragment() {
        boolean isNull = navController == null;
        logger.log("MainActivity navController is null: " + isNull);
        navController.navigate(R.id.action_splashFragment_to_mapFragment
                , null
                , new NavOptions.Builder()
                        .setPopUpTo(R.id.splashFragment, true)
                        .build()
        );
    }

}