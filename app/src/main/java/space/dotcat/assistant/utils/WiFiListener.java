package space.dotcat.assistant.utils;

import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

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
    }

    @Override
    protected void onInactive() {
        unsubscribeReceiver();
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
        if (mStateReceiver != null) mContext.unregisterReceiver(mStateReceiver);
    }
}
