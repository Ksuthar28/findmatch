package com.saadi.findmatch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Kailash Suthar.
 */

public class HelperMethod {

    /**
     * Get the network info
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = HelperMethod.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static String getUpperCaseWord(String type) {
        if (type.isEmpty()) return "";
        type = type.replace("_", " ");
        String[] strArray = type.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strArray.length; i++) {
            String cap = strArray[i].substring(0, 1).toUpperCase() + strArray[i].substring(1);
            if (i != strArray.length - 1) {
                builder.append(cap).append(" ");
            } else {
                builder.append(cap);
            }
        }
        return builder.toString();
    }
}
