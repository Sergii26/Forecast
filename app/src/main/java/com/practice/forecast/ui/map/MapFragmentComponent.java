package com.practice.forecast.ui.map;

import dagger.Component;

@Component(modules = {MapFragmentModule.class})
public interface MapFragmentComponent {
    void injectMapFragment(MapFragment fragment);
}
