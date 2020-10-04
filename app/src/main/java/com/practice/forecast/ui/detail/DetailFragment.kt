package com.practice.forecast.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.forecast.R
import com.practice.forecast.databinding.FragmentDetailBinding
import com.practice.forecast.ui.arch.mvi.MviFragment
import com.practice.forecast.ui.detail.DetailContract.BaseDetailViewModel
import com.practice.forecast.ui.detail.DetailFragment
import com.practice.forecast.ui.map.CityListAdapter
import com.practice.forecast.ui.map.WeatherMapViewModel
import com.practice.forecast.ui.map.WeatherViewModelFactory
import com.practice.forecast.ui.splash.SplashViewModel
import com.practice.weathermodel.location_api.LocationClient
import com.practice.weathermodel.logger.ILog
import com.practice.weathermodel.logger.Logger
import com.practice.weathermodel.network_api.ApiClient
import com.practice.weathermodel.pojo.City
import com.practice.weathermodel.prefs.PrefsImpl
import com.practice.weathermodel.receiver.NetworkStatusChangeReceiver
import com.practice.weathermodel.utils.AndroidUtils
import javax.inject.Inject

class DetailFragment : MviFragment<DetailScreenState?, DetailContract.Host?>(), DetailContract.View {
    private var progressLoading: ProgressBar? = null
    private val logger: ILog = Logger.withTag("MyLog")
    private var viewModel: BaseDetailViewModel? = null
    private var binding: FragmentDetailBinding? = null
    private val adapter = DetailRecyclerAdapter()
    private var rvDetail: RecyclerView? = null

//    @JvmField
//    @Inject
//    var viewModelFactory: ViewModelProvider.Factory? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (hasCallBack()) {
                callBack!!.backToMapFragment()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerDetailFragmentComponent.builder()
                .detailFragmentModule(DetailFragmentModule())
                .build()
                .injectDetailFragment(this)

        viewModel = viewModelFactory.let { ViewModelProvider(this, it).get(DetailViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding?.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressLoading = view.findViewById(R.id.progress_loading)
        rvDetail = view.findViewById(R.id.rvWeather)
        rvDetail?.setAdapter(adapter)
        rvDetail?.setLayoutManager(LinearLayoutManager(context, RecyclerView.HORIZONTAL, false))
        binding!!.cityName = requireArguments().getString(KEY_CITY_NAME)
        viewModel!!.downloadCity(requireArguments().getInt(KEY_CITY_ID).toString())
        viewModel!!.getStateHolderObservable().observe(viewLifecycleOwner, { detailScreenState: DetailScreenState? ->
            logger.log("DetailFragment onStateChange()")
            detailScreenState!!.visit(this@DetailFragment)
        })
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun showProgress() {
        progressLoading!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressLoading!!.visibility = View.INVISIBLE
    }

    override fun setListToAdapter(weatherLIst: List<City?>?) {
        binding!!.city = weatherLIst!![1]
        adapter.setCities(weatherLIst)
    }

    override fun showError() {
        Toast.makeText(activity, R.string.detail_error_state, Toast.LENGTH_SHORT).show()
    }

    @VisibleForTesting
    fun getRecyclerAdapter(): DetailRecyclerAdapter {
        return adapter;
    }

    companion object {
        const val KEY_CITY_ID = "key_city_id"
        const val KEY_CITY_NAME = "city_name"
    }
}