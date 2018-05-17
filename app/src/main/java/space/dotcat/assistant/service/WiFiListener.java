package space.dotcat.assistant.service;

import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WiFiListener extends LiveData<Boolean> {

    private final IntentFilter CONNECTIVITY_INTENT_FILTER = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    private Context mContext;

    private BroadcastReceiver mStateReceiver;

    public WiFiListener(Context context) {
        mContext = context;
    }

    @Override
    protected void onActive() {
        subscribeReceiver();

        Log.d(this.getClass().getName(), "Subscribed receiver, state - Active");
    }

    @Override
    protected void onInactive() {
        unsubscribeReceiver();

        Log.d(this.getClass().getName(), "Unsubscribed receiver, state - Inactive");
    }

    private void subscribeReceiver() {
        if (mStateReceiver == null) {
            mStateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

                    if (networkInfo != null) {
                        boolean isConnected = networkInfo.isConnected();

                        setValue(isConnected);
                    }
                }
            };
        }

        mContext.registerReceiver(mStateReceiver, CONNECTIVITY_INTENT_FILTER);
    }

    private void unsubscribeReceiver() {
        if (mStateReceiver != null)  {
            mContext.unregisterReceiver(mStateReceiver);

            mStateReceiver = null;
        }
    }
}
