package com.practice.forecast.ui.detail;

import android.content.pm.ActivityInfo;

import com.practice.forecast.R;
import com.practice.forecast.rule.OpenDetailFragmentRule;
import com.practice.forecast.ui.MainActivity;
import com.practice.forecast.util.TestInstruments;
import com.practice.weathermodel.pojo.City;

import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.practice.forecast.util.TestInstruments.getCountFromRecyclerView;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

public class DetailFragmentTest {

    @Rule
    public OpenDetailFragmentRule<MainActivity> mActivityRule =
            new OpenDetailFragmentRule<>(MainActivity.class);

    @Test
    public void viewsAreShown() {
        TestInstruments.waitTime(4000);
        openDetailFragment();
        onView(withId(R.id.progress_loading)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tvCityName)).check(matches(isDisplayed()));
        onView(withId(R.id.tvCurrentTemp)).check(matches(isDisplayed()));
        onView(withId(R.id.ivCurrentWeatherIcon)).check(matches(isDisplayed()));
        onView(withId(R.id.rvWeather)).check(matches(isDisplayed()));
    }

    @Test
    public void showingWeatherForChoseCity() {
        TestInstruments.waitTime(4000);
        String cityName = mActivityRule.getCitiesRecyclerList().get(0).getName();
        openDetailFragment();
        onView(allOf(withId(R.id.tvCityName), withText(cityName))).check(matches(isDisplayed()));
    }

    @Test
    public void correctLabelsTest(){
        TestInstruments.waitTime(4000);
        openDetailFragment();
        ViewInteraction view = onView(allOf(withId(R.id.rvWeather), isDisplayed()));
        view.check(matches(TestInstruments.atPosition(0, hasDescendant(allOf(withText(R.string.date_label), withId(R.id.tvDate))))));
        view.check(matches(TestInstruments.atPosition(0, hasDescendant(allOf(withText(R.string.time_label), withId(R.id.tvTime))))));
        view.check(matches(TestInstruments.atPosition(0, hasDescendant(allOf(withText("\u00B0" + "C"), withId(R.id.tvTemp))))));
        view.check(matches(TestInstruments.atPosition(0, hasDescendant(allOf(withText(R.string.wind_label), withId(R.id.tvWind))))));
        view.check(matches(TestInstruments.atPosition(0, hasDescendant(allOf(withText(R.string.pressure_label), withId(R.id.tvPressure))))));
        view.check(matches(TestInstruments.atPosition(0, hasDescendant(allOf(withText(R.string.humidity_label), withId(R.id.tvHumidity))))));
    }

    @Test
    public void correctRecyclerItemsTest() {
        TestInstruments.waitTime(4000);
        openDetailFragment();
        List<City> cities = mActivityRule.getWeatherRecyclerList();
        ViewInteraction view = onView(allOf(withId(R.id.rvWeather), isDisplayed()));
        for (int i = 1; i < getCountFromRecyclerView(R.id.rvWeather); i++) {
            view.perform(scrollToPosition(i));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(convertDate(cities.get(i).getDt()))), withId(R.id.tvDate))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(convertTime(cities.get(i).getDt()))), withId(R.id.tvTime))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf((int)cities.get(i).getMain().getTemp()) + "\u00B0"), withId(R.id.tvTemp))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf((int)cities.get(i).getWind().getSpeed())), withId(R.id.tvWind))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(cities.get(i).getMain().getPressure())), withId(R.id.tvPressure))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(cities.get(i).getMain().getHumidity())), withId(R.id.tvHumidity))))));
        }

    }
    
    @Test
    public void showingWeatherAfterOrientationChangeTest(){
        TestInstruments.waitTime(4000);
        openDetailFragment();
        List<City> cities = mActivityRule.getWeatherRecyclerList();
        ViewInteraction view = onView(allOf(withId(R.id.rvWeather), isDisplayed()));
        for (int i = 1; i < getCountFromRecyclerView(R.id.rvWeather); i++) {
            view.perform(scrollToPosition(i));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(convertDate(cities.get(i).getDt()))), withId(R.id.tvDate))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(convertTime(cities.get(i).getDt()))), withId(R.id.tvTime))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf((int)cities.get(i).getMain().getTemp()) + "\u00B0"), withId(R.id.tvTemp))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf((int)cities.get(i).getWind().getSpeed())), withId(R.id.tvWind))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(cities.get(i).getMain().getPressure())), withId(R.id.tvPressure))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(cities.get(i).getMain().getHumidity())), withId(R.id.tvHumidity))))));
        }
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        view = onView(allOf(withId(R.id.rvWeather), isDisplayed()));
        for (int i = 1; i < getCountFromRecyclerView(R.id.rvWeather); i++) {
            view.perform(scrollToPosition(i));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(convertDate(cities.get(i).getDt()))), withId(R.id.tvDate))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(convertTime(cities.get(i).getDt()))), withId(R.id.tvTime))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf((int)cities.get(i).getMain().getTemp()) + "\u00B0"), withId(R.id.tvTemp))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf((int)cities.get(i).getWind().getSpeed())), withId(R.id.tvWind))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(cities.get(i).getMain().getPressure())), withId(R.id.tvPressure))))));
            view.check(matches(TestInstruments.atPosition(i, hasDescendant(allOf(withText(String.valueOf(cities.get(i).getMain().getHumidity())), withId(R.id.tvHumidity))))));
        }
            
        
    }

    private void openDetailFragment() {
        onView(withId(R.id.rvCities))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    private String convertTime(int time){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("HH:mm");
        return sdf.format(time * 1000L);
    }

    private String convertDate(int time){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("EEEE");
        return sdf.format(time * 1000L);
    }

}
