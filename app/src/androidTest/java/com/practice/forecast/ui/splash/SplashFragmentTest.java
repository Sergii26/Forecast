package com.practice.forecast.ui.splash;

import com.practice.forecast.R;
import com.practice.forecast.rule.OpenSplashFragmentRule;
import com.practice.forecast.ui.MainActivity;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class SplashFragmentTest {
    @Rule
    public OpenSplashFragmentRule<MainActivity> mActivityRule =
            new OpenSplashFragmentRule<>(MainActivity.class);

    @Test
    public void viewsAreShown(){
        onView(allOf(withText(R.string.app_name), withId(R.id.tvAppName))).check(matches(isDisplayed()));
    }
}
