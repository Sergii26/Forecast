package com.practice.forecast.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.practice.forecast.R
import com.practice.forecast.ui.arch.mvvm.MvvmFragment
import com.practice.forecast.ui.map.MapContract.BaseMapViewModel
import com.practice.weathermodel.logger.ILog
import com.practice.weathermodel.logger.Logger
import com.practice.weathermodel.pojo.City
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MapFragment : MvvmFragment<MapContract.Host?>(), OnMapReadyCallback, OnCameraMoveListener {
    private val logger: ILog = Logger.withTag("MyLog")
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private lateinit var rvCities: RecyclerView
    private var viewModel: BaseMapViewModel? = null
    private val coordinatesObservable = BehaviorSubject.createDefault("")
    private val disposable = CompositeDisposable()
    private val adapter = CityListAdapter()
    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (hasCallBack()) callBack!!.closeApp()
        }
    }

    @Inject
    lateinit var  viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMapFragmentComponent.builder()
                .mapFragmentModule(MapFragmentModule())
                .build()
                .injectMapFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherMapViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        rvCities = view.findViewById(R.id.rvCities)
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            logger.log("MapFragment onViewCreated() portrait orientation")
            rvCities.setLayoutManager(GridLayoutManager(context, 3, RecyclerView.HORIZONTAL, false))
        }
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            logger.log("MapFragment onViewCreated() landscape orientation")
            rvCities.setLayoutManager(GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false))
        }
        rvCities.setAdapter(adapter)
        initMap()
        viewModel?.getCitiesWeather()?.observe(viewLifecycleOwner, { cities: List<City?>? -> adapter.setCities(cities) })
        viewModel?.getCurrentLocation()?.observe(viewLifecycleOwner, { location: LatLng? ->
            if (googleMap != null && location != null) {
                setCameraPosition(location)
            }
        })
        if (!isGrantedPermission) {
            requestPermission()
        }
        viewModel?.getConnectivityState()?.observe(viewLifecycleOwner, { isConnected: Boolean? ->
            if (isConnected!!) {
                rvCities.setVisibility(View.VISIBLE)
                mapView.setVisibility(View.VISIBLE)
            } else {
                rvCities.setVisibility(View.GONE)
                mapView.setVisibility(View.GONE)
            }
        })
        viewModel!!.showCities(coordinatesObservable)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        viewLifecycleOwner.lifecycle.addObserver(viewModel!!)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        if (disposable.size() == 0) {
            logger.log("MapFragment onStarT() adapter == null: " + (adapter == null))
            disposable.add(adapter.clicksObservable.subscribe({ city: City? ->
                logger.log("MapFragment onStarT() clickObservable() onSuccess")
                viewModel!!.saveLocation(googleMap!!.projection.visibleRegion.latLngBounds.center)
                if (hasCallBack()) {
                    callBack!!.openDetailFragment(city!!.name, city.id)
                }
            }) { throwable: Throwable -> logger.log("MapFragment onStarT() clickObservable() onError: " + throwable.message) })
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(googleMap != null){
            mapView.onDestroy()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onCameraMove() {
        coordinatesObservable.onNext(coordinatesForRequest)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        logger.log("MapFragment onMapReady()")
        this.googleMap = googleMap
        googleMap.setOnCameraMoveListener(this)
        viewModel!!.findCurrentLocation(isGrantedPermission)
        googleMap.setMinZoomPreference(8f)
    }

    private fun setCameraPosition(latLng: LatLng?) {
        logger.log("MapFragment setCameraPosition()")
        val cameraPosition = CameraPosition.Builder()
                .target(latLng).zoom(7f).build()
        googleMap!!.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition))
    }

    private fun initMap() {
        try {
            MapsInitializer.initialize(requireContext())
        } catch (e: Exception) {
            logger.log("MapFragment initMap() error: " + e.message)
        }
        mapView.getMapAsync(this)
    }

    private val coordinatesForRequest: String
        get() = (googleMap!!.projection.visibleRegion.latLngBounds.southwest.longitude.toString() + ","
                + googleMap!!.projection.visibleRegion.latLngBounds.southwest.latitude.toString() + ","
                + googleMap!!.projection.visibleRegion.latLngBounds.northeast.longitude.toString() + ","
                + googleMap!!.projection.visibleRegion.latLngBounds.northeast.latitude.toString() + ","
                + WEATHER_API_ZOOM_SIZE)

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        logger.log("MapFragment in onRequestPermissionsResult()")
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                logger.log("MapFragment in onRequestPermissionsResult() permission granted")
                viewModel!!.findCurrentLocation(true)
            } else {
                // permission denied
                logger.log("MapFragment in onRequestPermissionsResult() permission denied")
                Toast.makeText(activity, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun requestPermission() {
        logger.log("MapFragment requestPermission()")
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
    }

    val isGrantedPermission: Boolean
        get() {
            logger.log("MapFragment isGrantedPermission()")
            return activity != null && ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }

    @VisibleForTesting
    fun getRecyclerAdapter(): CityListAdapter {
        return adapter;
    }

    companion object {
        private const val WEATHER_API_ZOOM_SIZE = "17"
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}