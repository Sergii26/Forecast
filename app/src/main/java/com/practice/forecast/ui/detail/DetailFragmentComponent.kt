package com.practice.forecast.ui.detail

import dagger.Component


@Component(modules = [DetailFragmentModule::class])
interface DetailFragmentComponent {
    fun injectDetailFragment(fragment: DetailFragment?)
}