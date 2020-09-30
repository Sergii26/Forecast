package com.practice.forecast.ui.detail;

import com.practice.forecast.ui.arch.Contract;
import com.practice.forecast.ui.arch.mvi.ScreenState;
import com.practice.weathermodel.logger.ILog;
import com.practice.weathermodel.logger.Logger;
import com.practice.weathermodel.pojo.City;

import java.util.List;

public class DetailScreenState implements ScreenState<DetailContract.View> {
    private static final int LOADING = 1;
    private static final int SET_DATA = 2;
    private static final int ERROR = 3;

    private ILog logger = Logger.withTag("MyLog");
    private final int action;
    private final Throwable error;
    private final List<City> weatherList;

    public DetailScreenState(int action, Throwable error, List<City> weatherList) {
        this.action = action;
        this.error = error;
        this.weatherList = weatherList;
    }

    public static DetailScreenState createSetDataState(List<City> weatherLIst) {
        return new DetailScreenState(SET_DATA, null, weatherLIst);

    }

    public static DetailScreenState createLoadingState() {
        return new DetailScreenState(LOADING, null, null);
    }

    public static DetailScreenState createErrorState(Throwable error) {
        return new DetailScreenState(ERROR, error, null);
    }

    @Override
    public void visit(DetailContract.View screen) {
        logger.log("DetailScreenState visit() action: " + action);
        if (LOADING == action) {
            screen.showProgress();
            return;
        }
        screen.hideProgress();
        if(SET_DATA == action){
            screen.setListToAdapter(weatherList);
        } else if(ERROR == action){
            screen.showError();
        }
    }
}
