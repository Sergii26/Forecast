package com.practice.forecast.ui.map;

import android.Manifest;
import android.content.Intent;

import com.practice.forecast.R;
import com.practice.forecast.rule.OpenMapFragmentRule;
import com.practice.forecast.rule.OpenMapFragmentWithMockedUtilsRule;
import com.practice.forecast.ui.MainActivity;
import com.practice.forecast.util.TestInstruments;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

public class MapFragmentTestWithMockedUtils {

    @Rule
    public OpenMapFragmentWithMockedUtilsRule<MainActivity> mActivityRule =
            new OpenMapFragmentWithMockedUtilsRule<>(MainActivity.class);

    @Test
    public void viewAreGoneWithoutNetworkConnectionTest() {
        TestInstruments.waitTime(3000);
        onView(withId(R.id.rvCities)).check(matches(isDisplayed()));
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
        //mActivityRule.sendBroadcast(); - chang network connection status
        mActivityRule.sendBroadcast();
        TestInstruments.waitTime(3000);
        onView(withId(R.id.rvCities)).check(matches(not(isDisplayed())));
        onView(withId(R.id.mapView)).check(matches(not(isDisplayed())));
        mActivityRule.sendBroadcast();
        TestInstruments.waitTime(3000);
        onView(withId(R.id.rvCities)).check(matches(isDisplayed()));
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
    }

}
