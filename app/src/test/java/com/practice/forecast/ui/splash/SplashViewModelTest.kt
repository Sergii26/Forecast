package com.practice.forecast.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.practice.weathermodel.logger.ILog
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.Assert.assertEquals
import org.mockito.Mockito
import org.mockito.Mockito.mock

class SplashViewModelTest {
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

    private lateinit var viewModel: SplashViewModel
    private lateinit var logger: ILog

    @Before
    fun setup() {
        logger = mock(ILog::class.java)
        viewModel = SplashViewModel(logger)
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
    fun startTimerTest(){
        viewModel.startTimer()
        assertEquals(false, viewModel.getIsReady().value)
        Thread.sleep(1200)
        assertEquals(true, viewModel.getIsReady().value)
    }
}

