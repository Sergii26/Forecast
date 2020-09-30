package com.practice.forecast.ui.arch.mvi

import com.practice.forecast.ui.arch.Contract

interface ScreenState<T : Contract.View?> {
    fun visit(screen: T)
}