package com.practice.forecast.ui.arch.mvi;

import com.practice.forecast.ui.arch.Contract;

public interface ScreenState<T extends Contract.View> {
    void visit(T screen);
}
