package com.practice.forecast.model.test_utils;

import com.practice.weathermodel.utils.Utils;

public class TestAndroidUtils implements Utils {

    int counter = 0;

    @Override
    public boolean isConnectedToNetwork() {
        counter++;
        return counter != 1;
    }
}
