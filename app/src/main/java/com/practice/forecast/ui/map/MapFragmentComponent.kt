package com.practice.forecast.ui.map

import dagger.Component


@Component(modules = [MapFragmentModule::class])
interface MapFragmentComponent {
    fun injectMapFragment(fragment: MapFragment?)
}