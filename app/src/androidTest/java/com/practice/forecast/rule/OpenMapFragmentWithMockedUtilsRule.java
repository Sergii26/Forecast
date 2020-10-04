package com.practice.forecast.rule;

import android.app.Activity;
import android.content.Intent;

import com.practice.forecast.App;
import com.practice.forecast.DaggerTestAppComponent;

import androidx.test.rule.ActivityTestRule;

public class OpenMapFragmentWithMockedUtilsRule <T extends Activity> extends ActivityTestRule {
    public OpenMapFragmentWithMockedUtilsRule(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        App.Companion.getInstance().setAppComponent(DaggerTestAppComponent.create());
    }

    public void sendBroadcast(){
            final Intent intent = new Intent("test.networkStatusConnectionChange");
            getActivity().sendBroadcast(intent);
    }
}
