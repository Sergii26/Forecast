package com.practice.forecast.ui.map

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.practice.weathermodel.location_api.LocationSupplier
import com.practice.weathermodel.location_api.Result
import com.practice.weathermodel.logger.ILog
import com.practice.weathermodel.network_api.NetworkClient
import com.practice.weathermodel.pojo.City
import com.practice.weathermodel.pojo.Response
import com.practice.weathermodel.prefs.Prefs
import com.practice.weathermodel.receiver.NetworkReceiver
import com.practice.weathermodel.utils.Utils
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.junit.*
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class MapWeatherViewModelTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun initialize() {

            // override AndroidSchedulers.mainThread() which doesn't work in unit tests
            RxAndroidPlugins.setInitMainThreadSchedulerHandler {
                Schedulers.trampoline()
            }
        }
    }

    @Suppress("unused")
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val lifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
    private val lifecycleRegistry = LifecycleRegistry(lifecycleOwner).also {
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(it)
    }

    private lateinit var networkClient: NetworkClient
    private lateinit var locationSupplier: LocationSupplier
    private lateinit var logger: ILog
    private lateinit var prefs: Prefs
    private lateinit var utils: Utils
    private lateinit var networkReceiver: NetworkReceiver
    private lateinit var viewModel: WeatherMapViewModel

    @Before
    fun setup() {
        networkClient = mock(NetworkClient::class.java)
        locationSupplier = mock(LocationSupplier::class.java)
        logger = mock(ILog::class.java)
        prefs = mock(Prefs::class.java)
        utils = mock(Utils::class.java)
        networkReceiver = mock(NetworkReceiver::class.java)
//      needs this mock for Lifecycle.Event.ON_RESUME
        `when`(networkReceiver.statusChangeObservable).thenReturn(Observable.just(1))
        viewModel = WeatherMapViewModel(networkClient, locationSupplier, logger, prefs, utils, networkReceiver)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycleRegistry.addObserver(viewModel)
    }

    @After
    fun teardown() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    @Test
    fun findCurrentLocationTest_WhitEmptyStringAtPrefs(){
        val location: Location = mock(Location::class.java)
        `when`(location.latitude).thenReturn(1.0)
        `when`(location.longitude).thenReturn(1.0)
        val result: Result<Location> = Result<Location>(location)
        val lastLocationObservable: Observable<Result<Location>> = Observable.just(result)
        `when`(locationSupplier.lastLocationObservable).thenReturn(lastLocationObservable)
        `when`(prefs.coordinates).thenReturn("")

        viewModel.findCurrentLocation(true)
        Thread.sleep(200)
        Assert.assertEquals(1.0, viewModel.getCurrentLocation().value?.longitude)
        Assert.assertEquals(1.0, viewModel.getCurrentLocation().value?.latitude)
    }

    @Test
    fun findCurrentLocationTest_WhitSavedCoordinatesAtPrefs(){
        `when`(prefs.coordinates).thenReturn("1;1")
        viewModel.findCurrentLocation(true)
        Thread.sleep(200)
        Assert.assertEquals(1.0, viewModel.getCurrentLocation().value?.longitude)
        Assert.assertEquals(1.0, viewModel.getCurrentLocation().value?.latitude)
    }

    @Test
    fun showCitiesTest(){
        val coordinates = "test"
        val city: City = City()
        city.id = 10
        val cities: MutableList<City> = ArrayList()
        cities.add(0, city)
        val response: Response = Response()
        response.cities = cities
        val coordinatesObservable: BehaviorSubject<String> = BehaviorSubject.create()
        `when`(networkClient.getCitiesWeatherWithinRectangle(coordinates)).thenReturn(Single.just(response))
        viewModel.showCities(coordinatesObservable)
        coordinatesObservable.onNext(coordinates)
        Thread.sleep(1200)
        Assert.assertEquals(10, viewModel.getCitiesWeather().value?.get(0)?.id)
    }

}
