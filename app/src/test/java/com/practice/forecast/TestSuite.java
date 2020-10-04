package com.practice.forecast;

import com.practice.forecast.ui.detail.DetailViewModelTest;
import com.practice.forecast.ui.map.MapWeatherViewModelTest;
import com.practice.forecast.ui.splash.SplashViewModelTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({SplashViewModelTest.class, MapWeatherViewModelTest.class, DetailViewModelTest.class})
public class TestSuite {
}
