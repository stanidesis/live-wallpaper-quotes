package com.stanleyidesis.quotograph.api.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.stanleyidesis.quotograph.LWQApplication;
import com.stanleyidesis.quotograph.api.event.NetworkConnectivityEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Copyright (c) 2016 Stanley Idesis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 * NetworkConnectionListener.java
 * @author Stanley Idesis
 *
 * From Quotograph
 * https://github.com/stanidesis/quotograph
 *
 * Please report any issues
 * https://github.com/stanidesis/quotograph/issues
 *
 * Date: 12/07/2015
 */
public class NetworkConnectionListener {

    private static String TAG = NetworkConnectionListener.class.getSimpleName();
    private static NetworkConnectionListener sNetworkConnectionListener;

    public static NetworkConnectionListener get() {
        if (sNetworkConnectionListener != null) {
            return sNetworkConnectionListener;
        }
        sNetworkConnectionListener = new NetworkConnectionListener();
        return get();
    }

    public enum ConnectionType {
        CONNECTION_NO_NETWORK,
        CONNECTION_UNKNOWN,
        CONNECTION_WIFI,
        CONNECTION_MOBILE_DATA;

        public boolean isConnected() {
            return this != CONNECTION_NO_NETWORK;
        }

    }

    private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.d(TAG, "Network connectivity change");
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                notifyConnectionUpdate();
            }
        }
    };

    private NetworkConnectionListener() {
        initialize(LWQApplication.get());
    }

    private void initialize(Context context) {
        context.registerReceiver(connectionReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public ConnectionType getConnectionType() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) LWQApplication.get().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        if (netInfo != null) {
            Log.d(TAG, netInfo.toString());
        }
        if (netInfo == null || !netInfo.isAvailable() || !netInfo.isConnected()) {
            return ConnectionType.CONNECTION_NO_NETWORK;
        } else if ((netInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    || (netInfo.getType() == ConnectivityManager.TYPE_WIMAX)) {
                return ConnectionType.CONNECTION_WIFI;
        } else if ((netInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                    || (netInfo.getType() == ConnectivityManager.TYPE_MOBILE_DUN)) {
                return ConnectionType.CONNECTION_MOBILE_DATA;
        } else {
            return ConnectionType.CONNECTION_UNKNOWN;
        }
    }

    private void notifyConnectionUpdate() {
        EventBus.getDefault().post(new NetworkConnectivityEvent(getConnectionType()));
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            LWQApplication.get().unregisterReceiver(connectionReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finalize();
    }
}
