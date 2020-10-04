package com.practice.forecast.ui.map;

import android.content.pm.ActivityInfo;

import com.practice.forecast.R;
import com.practice.forecast.rule.OpenMapFragmentRule;
import com.practice.forecast.ui.MainActivity;
import com.practice.forecast.util.TestInstruments;
import com.practice.weathermodel.pojo.City;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import androidx.test.espresso.ViewInteraction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.practice.forecast.util.TestInstruments.getCountFromRecyclerView;
import static org.hamcrest.core.AllOf.allOf;

public class MapFragmentTest {
    @Rule
    public OpenMapFragmentRule<MainActivity> mActivityRule =
            new OpenMapFragmentRule<>(MainActivity.class);

    @Test
    public void viewsAreShown() {
        TestInstruments.waitTime(3000);
        onView(withId(R.id.rvCities)).check(matches(isDisplayed()));
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
    }

    @Test
    public void citiesListAreShownTest() {
        TestInstruments.waitTime(3000);
        citiesAreShown();
    }

    @Test
    public void citiesAreShownAfterOrientationChangeTest() {
        TestInstruments.waitTime(3000);
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        TestInstruments.waitTime(3000);
        citiesAreShown();
    }

    private void citiesAreShown() {
        List<City> list = mActivityRule.getRecyclerList();
        onView(withId(R.id.rvCities)).check(matches(isDisplayed()));

        ViewInteraction view = onView(allOf(withId(R.id.rvCities), isDisplayed()));
        for (int i = 0; i < getCountFromRecyclerView(R.id.rvCities); i++) {
            view.perform(scrollToPosition(i));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(withText(list.get(i).getName())))));
        }
    }

}
