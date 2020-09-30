package com.practice.forecast.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.practice.forecast.R
import com.practice.forecast.ui.detail.DetailContract
import com.practice.forecast.ui.detail.DetailFragment
import com.practice.forecast.ui.map.MapContract
import com.practice.forecast.ui.splash.SplashContract
import com.practice.weathermodel.logger.ILog
import com.practice.weathermodel.logger.Logger

class MainActivity : AppCompatActivity(), SplashContract.Host, MapContract.Host, DetailContract.Host {
    private val logger: ILog = Logger.withTag("MyLog")
    private var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        logger.log("MainActivity onStart() navController is null: " + (navController == null))
    }

    override fun backToMapFragment() {
        logger.log("MainActivity backToMapFragment()")
        navController!!.navigate(R.id.action_detailFragment_to_mapFragment)
    }

    override fun openDetailFragment(cityName: String?, cityId: Int) {
        logger.log("MainActivity openDetailFragment()")
        val args = Bundle()
        args.putString(DetailFragment.KEY_CITY_NAME, cityName)
        args.putInt(DetailFragment.KEY_CITY_ID, cityId)
        navController!!.navigate(R.id.action_mapFragment_to_detailFragment, args)
    }

    override fun closeApp() {
        logger.log("MainActivity closeApp()")
        finish()
    }

    override fun openMapFragment() {
        logger.log("MainActivity openMapFragment()")
        navController!!.navigate(R.id.action_splashFragment_to_mapFragment, null, NavOptions.Builder()
                .setPopUpTo(R.id.splashFragment, true)
                .build()
        )
    }
}