package com.example.pial_pc.instantbookreview;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Pial-PC on 1/8/2016.
 */
public class InternetConnection extends Activity {
    /**
     * This method checks if there is a internet connection or not
     *
     * @param context
     * @return boolean
     */
    public boolean Checking_Internet_Connection(Context context) {
        boolean hasConnection = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (connectivity != null) {
            if (mMobile.isConnected()) {
                hasConnection = true;
            } else if (mWifi.isConnected()) {
                hasConnection = true;
            } else {
                hasConnection = false;
            }
        }

        return hasConnection;
    }

    /**
     * This method turns on internet data programmetically
     *
     * @param context
     * @throws Exception
     */
    public void Turning_internetData(Context context) throws Exception {
        try {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(telephonyService, true);
            }
        } catch (Exception ex) {
            Log.e("Internet", "Error setting mobile data state", ex);
        }
    }
}

