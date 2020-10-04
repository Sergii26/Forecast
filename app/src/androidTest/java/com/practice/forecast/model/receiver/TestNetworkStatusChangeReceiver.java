package com.practice.forecast.model.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.practice.weathermodel.receiver.NetworkStatusChangeReceiver;

public class TestNetworkStatusChangeReceiver extends NetworkStatusChangeReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("test.networkStatusConnectionChange".equals(intent.getAction())){
            statusChangeObservable.onNext(1);
        }
    }

    @Override
    public IntentFilter getIntentFilter() {
    //mocked intent filter for trigger receiver
        return new IntentFilter("test.networkStatusConnectionChange");
    }
}
