package com.practice.forecast.rule;

import android.app.Activity;

import com.practice.forecast.R;
import com.practice.forecast.ui.MainActivity;
import com.practice.forecast.ui.detail.DetailFragment;
import com.practice.forecast.ui.map.MapFragment;
import com.practice.weathermodel.pojo.City;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.test.rule.ActivityTestRule;

public class OpenDetailFragmentRule<T extends Activity> extends ActivityTestRule {
    public OpenDetailFragmentRule(Class activityClass) {
        super(activityClass);
    }

    public List<City> getCitiesRecyclerList(){
        MainActivity activity = (MainActivity) getActivity();
        NavHostFragment navHostFragment = (NavHostFragment) activity.getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        List<Fragment> fragments = navHostFragment.getChildFragmentManager().getFragments();
        MapFragment mapFragment = (MapFragment) fragments.get(fragments.size() - 1);
        return mapFragment.getRecyclerAdapter().getCities();
    }

    public List<City> getWeatherRecyclerList(){
        MainActivity activity = (MainActivity) getActivity();
        NavHostFragment navHostFragment = (NavHostFragment) activity.getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        List<Fragment> fragments = navHostFragment.getChildFragmentManager().getFragments();
        DetailFragment fragment = (DetailFragment) fragments.get(fragments.size() - 1);
        return fragment.getRecyclerAdapter().getWeatherList();
    }
}
