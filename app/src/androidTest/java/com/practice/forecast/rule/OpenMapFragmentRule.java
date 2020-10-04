package com.practice.forecast.rule;

import android.app.Activity;

import com.practice.forecast.R;
import com.practice.forecast.ui.MainActivity;
import com.practice.forecast.ui.map.MapFragment;
import com.practice.weathermodel.pojo.City;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.test.rule.ActivityTestRule;

public class OpenMapFragmentRule<T extends Activity> extends ActivityTestRule {
    public OpenMapFragmentRule(Class activityClass) {
        super(activityClass);
    }

    public List<City> getRecyclerList(){
        MainActivity activity = (MainActivity) getActivity();
        NavHostFragment navHostFragment = (NavHostFragment) activity.getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        List<Fragment> fragments = navHostFragment.getChildFragmentManager().getFragments();
        MapFragment mapFragment = (MapFragment) fragments.get(fragments.size() - 1);
        return mapFragment.getRecyclerAdapter().getCities();
    }

}

