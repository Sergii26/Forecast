package com.practice.forecast.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.practice.forecast.R
import com.practice.forecast.ui.arch.mvvm.MvvmFragment
import com.practice.forecast.ui.splash.SplashContract.BaseSplashViewModel
import com.practice.weathermodel.logger.ILog
import com.practice.weathermodel.logger.Logger
import javax.inject.Inject

class SplashFragment : MvvmFragment<SplashContract.Host?>() {
    private var viewModel: BaseSplashViewModel? = null
    private val logger: ILog = Logger.withTag("MyLog")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSplashFragmentComponent.builder()
                .splashFragmentModule(SplashFragmentModule())
                .build()
                .injectSplashFragment(this)
        viewModel = viewModelFactory.let { ViewModelProvider(this, it).get(SplashViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.getIsReady()?.observe(viewLifecycleOwner, Observer { aBoolean: Boolean? ->
            logger.log("SplashFragment onReady() isReady= $aBoolean")
            if (aBoolean!! && hasCallBack()) {
                callBack!!.openMapFragment()
            }
        })
        
        viewModel!!.startTimer()
        val splash = view.findViewById<TextView>(R.id.tvAppName)
        splash.startAnimation(animation)
    }

    private val animation: Animation
        get() {
            val animation = AnimationUtils.loadAnimation(context, R.anim.splash_anim)
            animation.interpolator = LinearInterpolator()
            animation.repeatCount = Animation.INFINITE
            animation.duration = 700
            return animation
        }
}