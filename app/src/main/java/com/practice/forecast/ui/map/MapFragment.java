package com.practice.forecast.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.practice.forecast.R;
import com.practice.forecast.ui.arch.mvvm.MvvmFragment;
import com.practice.weathermodel.logger.ILog;
import com.practice.weathermodel.logger.Logger;

import java.util.Objects;

import javax.inject.Inject;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class MapFragment extends MvvmFragment<MapContract.Host> implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {
    private final static String WEATHER_API_ZOOM_SIZE = "17";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private final ILog logger = Logger.withTag("MyLog");
    private MapView mapView;
    private GoogleMap googleMap;
    private RecyclerView rvCities;
    private MapContract.BaseMapViewModel viewModel;
    private final BehaviorSubject<String> coordinatesObservable = BehaviorSubject.createDefault("");
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final CityListAdapter adapter = new CityListAdapter();
    private final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if(hasCallBack()) getCallBack().closeApp();
        }
    };

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMapFragmentComponent.builder()
                .mapFragmentModule(new MapFragmentModule())
                .build()
                .injectMapFragment(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(WeatherMapViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        rvCities = view.findViewById(R.id.rvCities);
        if (requireActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            logger.log("MapFragment onViewCreated() portrait orientation");
            rvCities.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.HORIZONTAL, false));
        }
        if (requireActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            logger.log("MapFragment onViewCreated() landscape orientation");
            rvCities.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.HORIZONTAL, false));
        }
        rvCities.setAdapter(adapter);
        initMap();
        viewModel.getCitiesWeather().observe(getViewLifecycleOwner(), adapter::setCities);
        viewModel.getCurrentLocation().observe(getViewLifecycleOwner(), location -> {
            if (googleMap != null && location != null) {
                setCameraPosition(location);
            }
        });
        if (!isGrantedPermission()) {
            requestPermission();
        }
        viewModel.getConnectivityState().observe(getViewLifecycleOwner(), isConnected -> {
            if (isConnected) {
                rvCities.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.VISIBLE);
            } else {
                rvCities.setVisibility(View.GONE);
                mapView.setVisibility(View.GONE);
            }
        });
        viewModel.showCities(coordinatesObservable);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        getViewLifecycleOwner().getLifecycle().addObserver(viewModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        if (disposable.size() == 0) {
            disposable.add(adapter.getClicksObservable().subscribe(city -> {
                logger.log("MapFragment clickObservable() onSuccess");
                viewModel.saveLocation(googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter());
                if(hasCallBack()) {
                    getCallBack().openDetailFragment(city.getName(), city.getId());
                }
            }, throwable -> logger.log("MapFragment clickObservable() onError: " + throwable.getMessage())));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onCameraMove() {
        coordinatesObservable.onNext(getCoordinatesForRequest());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        logger.log("MapFragment onMapReady()");
        this.googleMap = googleMap;
        googleMap.setOnCameraMoveListener(this);
        viewModel.findCurrentLocation(isGrantedPermission());
        googleMap.setMinZoomPreference(8F);
    }

    public void setCameraPosition(LatLng latLng) {
        logger.log("MapFragment setCameraPosition()");
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(7).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    public void initMap() {
        try {
            MapsInitializer.initialize(requireContext());
        } catch (Exception e) {
            logger.log("MapFragment initMap() error: " + e.getMessage());
        }
        mapView.getMapAsync(this);
    }

    public String getCoordinatesForRequest() {
        return String.valueOf(googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.longitude)
                + ","
                + String.valueOf(googleMap.getProjection().getVisibleRegion().latLngBounds.southwest.latitude)
                + ","
                + String.valueOf(googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.longitude)
                + ","
                + String.valueOf(googleMap.getProjection().getVisibleRegion().latLngBounds.northeast.latitude)
                + ","
                + WEATHER_API_ZOOM_SIZE;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        logger.log("MapFragment in onRequestPermissionsResult()");
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                logger.log("MapFragment in onRequestPermissionsResult() permission granted");
                viewModel.findCurrentLocation(true);
            } else {
                // permission denied
                logger.log("MapFragment in onRequestPermissionsResult() permission denied");
                Toast.makeText(getActivity(), getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void requestPermission() {
        logger.log("MapFragment requestPermission()");
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
    }

    public boolean isGrantedPermission() {
        logger.log("MapFragment isGrantedPermission()");
        return getActivity() != null && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
