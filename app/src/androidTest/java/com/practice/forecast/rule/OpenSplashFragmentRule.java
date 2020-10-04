package com.practice.forecast.rule;

import android.app.Activity;

import com.practice.forecast.ui.MainActivity;

import androidx.test.rule.ActivityTestRule;

public class OpenSplashFragmentRule<T extends Activity> extends ActivityTestRule {
    public OpenSplashFragmentRule(Class activityClass) {
        super(activityClass);
    }

}
