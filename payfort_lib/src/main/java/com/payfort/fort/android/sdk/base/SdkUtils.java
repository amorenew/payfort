/*    */
package com.payfort.fort.android.sdk.base;
/*    */ 
/*    */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.payfort.sdk.android.dependancies.models.SdkResponse;
import com.payfort.sdk.android.dependancies.utils.Utils;

import java.util.Map;

import amorenew.com.payfort_lib.R;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */

/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SdkUtils
/*    */ {
    /*    */
    public static String getOsDetails()
/*    */ {
/* 24 */
        return "OS = ANDROID, MANUFACTURER = " + Build.MANUFACTURER + ", MODEL = " + Build.MODEL + ", BASE_OS = " + Build.BOOTLOADER + ", SDK_INT = " + Build.VERSION.SDK_INT + ", VERSION_CODES.BASE = " + 1;
/*    */
    }

    /*    */
/*    */ 
/*    */
    public static boolean haveNetworkConnection(Context context)
/*    */ {
/* 30 */
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
/*    */
        NetworkInfo networkInfo;
/* 32 */
        if (Build.VERSION.SDK_INT >= 21) {
/* 33 */
            Network[] networks = connectivityManager.getAllNetworks();
/*    */       
/* 35 */
            for (Network mNetwork : networks) {
/* 36 */
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
/* 37 */
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
/* 38 */
                    return true;
/*    */
                }
/*    */
            }
/*    */
        }
/* 42 */
        else if (connectivityManager != null)
/*    */ {
/* 44 */
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
/* 45 */
            if (info != null) {
/* 46 */
                for (NetworkInfo anInfo : info) {
/* 47 */
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
/* 48 */
                        return true;
/*    */
                    }
/*    */
                }
/*    */
            }
/*    */
        }
/*    */     
/* 54 */
        return false;
/*    */
    }

    /*    */
/*    */
    public static SdkResponse collectResponse(Context context, String jsonResponse, Map<String, String> merchantRequestMap) {
/* 58 */
        if (jsonResponse == null) {
/* 59 */
            return Utils.collectResponse(context.getResources().getString(R.string.pf_no_connection), jsonResponse, merchantRequestMap);
/*    */
        }
/* 61 */
        return Utils.collectResponse(context.getResources().getString(R.string.pf_technical_problem), jsonResponse, merchantRequestMap);
/*    */
    }
/*    */
}
