package com.practice.forecast.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.practice.forecast.ui.detail.DetailScreenState
import com.practice.forecast.ui.detail.DetailViewModel
import com.practice.weathermodel.logger.ILog
import com.practice.weathermodel.network_api.NetworkClient
import com.practice.weathermodel.pojo.Response
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class Nextdd {
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
        `when`(lifecycleOwner.lifecycle).thenReturn(it)
    }

    private lateinit var networkClient: NetworkClient
    private lateinit var logger: ILog
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup() {
        networkClient = Mockito.mock(NetworkClient::class.java)
        logger = Mockito.mock(ILog::class.java)
        viewModel = DetailViewModel(logger, networkClient)
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
    fun downloadCity_WithResult(){
        val cityId = "1"
        `when`(networkClient.getCityWeatherById(cityId)).thenReturn(Single.just(mock(Response::class.java)))
        viewModel.downloadCity("1")
        Assert.assertEquals(DetailScreenState.Companion.LOADING, viewModel.getStateHolderObservable().value?.getAction())
        Thread.sleep(200)
        Assert.assertEquals(DetailScreenState.Companion.SET_DATA, viewModel.getStateHolderObservable().value?.getAction())
    }

    @Test
    fun downloadCity_WithError(){
        `when`(networkClient.getCityWeatherById("1")).thenReturn(Single.error(Exception()))
        viewModel.downloadCity("1")
        Assert.assertEquals(DetailScreenState.Companion.LOADING, viewModel.getStateHolderObservable().value?.getAction())
        Thread.sleep(200)
        Assert.assertEquals(DetailScreenState.Companion.ERROR, viewModel.getStateHolderObservable().value?.getAction())
    }
}