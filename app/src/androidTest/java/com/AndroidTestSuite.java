package com;

import com.practice.forecast.ui.detail.DetailFragmentTest;
import com.practice.forecast.ui.map.MapFragmentTest;
import com.practice.forecast.ui.map.MapFragmentTestWithMockedUtils;
import com.practice.forecast.ui.splash.SplashFragmentTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SplashFragmentTest.class, MapFragmentTest.class, MapFragmentTestWithMockedUtils.class, DetailFragmentTest.class})
public class AndroidTestSuite {
}
