package com.practice.forecast.ui.detail

import com.practice.forecast.ui.arch.mvi.ScreenState
import com.practice.weathermodel.logger.ILog
import com.practice.weathermodel.logger.Logger
import com.practice.weathermodel.pojo.City

class DetailScreenState(private val action: Int, private val error: Throwable?, private val weatherList: List<City?>?) : ScreenState<DetailContract.View?> {
    private val logger: ILog = Logger.withTag("MyLog")
//    override fun visit(screen: DetailContract.View) {
//        logger.log("DetailScreenState visit() action: $action")
//        if (LOADING == action) {
//            screen.showProgress()
//            return
//        }
//        screen.hideProgress()
//        if (SET_DATA == action) {
//            screen.setListToAdapter(weatherList)
//        } else if (ERROR == action) {
//            screen.showError()
//        }
//    }

    companion object {
        private const val LOADING = 1
        private const val SET_DATA = 2
        private const val ERROR = 3
        fun createSetDataState(weatherLIst: List<City?>?): DetailScreenState {
            return DetailScreenState(SET_DATA, null, weatherLIst)
        }

        fun createLoadingState(): DetailScreenState {
            return DetailScreenState(LOADING, null, null)
        }

        fun createErrorState(error: Throwable?): DetailScreenState {
            return DetailScreenState(ERROR, error, null)
        }
    }

    override fun visit(screen: DetailContract.View?) {
        logger.log("DetailScreenState visit() action: $action")
        if (LOADING == action) {
            screen?.showProgress()
            return
        }
        screen?.hideProgress()
        if (SET_DATA == action) {
            screen?.setListToAdapter(weatherList)
        } else if (ERROR == action) {
            screen?.showError()
        }
    }
}
