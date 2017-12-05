/*     */
package com.payfort.fort.android.sdk.base;
/*     */ 
/*     */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.payfort.fort.android.sdk.activities.InitSecureConnectionActivity;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
import com.payfort.sdk.android.dependancies.base.FortInterfaces;
import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.payfort.sdk.android.dependancies.models.SdkResponse;
import com.payfort.sdk.android.dependancies.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FortSdk
/*     */ {
    /*     */   private static volatile FortSdk instance;

    /*     */
/*     */
    public static FortSdk getInstance()
/*     */ {
/*  43 */
        if (instance == null) {
/*  44 */
            synchronized (FortSdk.class) {
/*  45 */
                if (instance == null) {
/*  46 */
                    instance = new FortSdk();
/*     */
                }
/*     */
            }
/*     */
        }
/*  50 */
        return instance;
/*     */
    }

    /*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
    public static String getDeviceId(Context context)
/*     */ {
/* 176 */
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
/*     */
        String tmSerial;
/* 178 */
        String tmDevice;
        if (Build.VERSION.SDK_INT >= 23) {
/*     */
/* 180 */
            if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
/* 181 */
                if (((context instanceof Activity)) &&
/* 182 */           (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.READ_PHONE_STATE"))) {
/* 183 */
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_PHONE_STATE"}, 222);
/*     */
                }
/*     */
/* 186 */
                tmDevice = "";
/* 187 */
                tmSerial = "";
/*     */
            } else {
/* 189 */
                tmDevice = "" + tm.getDeviceId();
/* 190 */
                tmSerial = "" + tm.getSimSerialNumber();
/*     */
            }
/*     */
        } else {
/* 193 */
            tmDevice = "" + tm.getDeviceId();
/* 194 */
            tmSerial = "" + tm.getSimSerialNumber();
/*     */
        }
/* 196 */
        String androidId = "" + Settings.Secure.getString(context.getContentResolver(), "android_id");
/* 197 */
        UUID deviceUuid = new UUID(androidId.hashCode(), tmDevice.hashCode() << 32 | tmSerial.hashCode());
/* 198 */
        return deviceUuid.toString();
/*     */
    }

    /*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
    @Deprecated
/*     */ public void registerCallback(Activity context, final FortRequest fortRequest, String environment, final int requestCode, FortCallBackManager callbackManager, final FortInterfaces.OnTnxProcessed callback)
/*     */     throws Exception
/*     */ {
/*  66 */
        if (!(callbackManager instanceof FortCallback)) {
/*  67 */
            throw new Exception("Unexpected CallbackManager, please use the provided Factory.");
/*     */
        }
/*     */
/*     */
/*  71 */
        ((FortCallback) callbackManager).registerCallback(requestCode, new FortCallback.Callback()
/*     */ {
            /*     */
            public boolean onActivityResult(int mRequestCode, int resultCode, Intent data) {
/*  74 */
                if (requestCode == mRequestCode) {
/*  75 */
                    return FortSdk.this.onActivityResult(fortRequest
/*  76 */.getRequestMap(), resultCode, data, callback);
/*     */
                }
/*     */
/*     */
/*     */
/*  81 */
                return false;
/*     */
/*     */
            }
/*     */
/*     */
/*  86 */
        });
/*  87 */
        Intent openSDK = new Intent(context, InitSecureConnectionActivity.class);
/*  88 */
        openSDK.putExtra("merchantReq", fortRequest);
/*  89 */
        openSDK.putExtra("environment", environment);
/*  90 */
        openSDK.putExtra("showLoading", true);
/*  91 */
        context.startActivityForResult(openSDK, requestCode);
/*     */
    }

    /*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
    public void registerCallback(Activity context, final FortRequest fortRequest, String environment, final int requestCode, FortCallBackManager callbackManager, boolean showLoading, final FortInterfaces.OnTnxProcessed callback)
/*     */     throws Exception
/*     */ {
/* 106 */
        if (!(callbackManager instanceof FortCallback)) {
/* 107 */
            throw new Exception("Unexpected CallbackManager, please use the provided Factory.");
/*     */
        }
/*     */
/*     */
/* 111 */
        ((FortCallback) callbackManager).registerCallback(requestCode, new FortCallback.Callback()
/*     */ {
            /*     */
            public boolean onActivityResult(int mRequestCode, int resultCode, Intent data) {
/* 114 */
                if (requestCode == mRequestCode) {
/* 115 */
                    return FortSdk.this.onActivityResult(fortRequest
/* 116 */.getRequestMap(), resultCode, data, callback);
/*     */
                }
/*     */
/*     */
/*     */
/* 121 */
                return false;
/*     */
/*     */
            }
/*     */
/*     */
/* 126 */
        });
/* 127 */
        Intent openSDK = new Intent(context, InitSecureConnectionActivity.class);
/* 128 */
        openSDK.putExtra("merchantReq", fortRequest);
/* 129 */
        openSDK.putExtra("environment", environment);
/* 130 */
        openSDK.putExtra("showLoading", showLoading);
/* 131 */
        context.startActivityForResult(openSDK, requestCode);
/*     */
    }

    /*     */
/*     */   boolean onActivityResult(Map<String, String> requestParams, int resultCode, Intent data) {
/* 135 */
        return onActivityResult(requestParams, resultCode, data, null);
/*     */
    }

    /*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   boolean onActivityResult(Map<String, String> requestParams, int resultCode, Intent data, FortInterfaces.OnTnxProcessed tnxProcessedCallback)
/*     */ {
/* 147 */
        if (data == null || data.getExtras() == null)
            return false;

        if (!data.getExtras().containsKey("sdkResp"))
/*     */ {
/* 149 */
            Map<String, String> responseMap = new HashMap(requestParams);
/* 150 */
            responseMap.put("status", "00");
/* 151 */
            responseMap.put("response_code", "00072");
/* 152 */
            if (Utils.getParamValue(responseMap, "language").equals("ar")) {
/* 153 */
                responseMap.put("response_message", "تم الغاء العملية من المشتري");
/*     */
            } else {
/* 155 */
                responseMap.put("response_message", "Transaction canceled by payer");
/*     */
            }
/* 157 */
            tnxProcessedCallback.onCancel(requestParams, responseMap);
/* 158 */
            return false;
/*     */
        }
/* 160 */
        SdkResponse response = (SdkResponse) data.getSerializableExtra("sdkResp");
/* 161 */
        if (response.isSuccess()) {
/* 162 */
            tnxProcessedCallback.onSuccess(requestParams, response.getResponseMap());
/*     */
        } else {
/* 164 */
            tnxProcessedCallback.onFailure(requestParams, response.getResponseMap());
/*     */
        }
/* 166 */
        return false;
/*     */
    }

    /*     */
/*     */   public static class ENVIRONMENT
/*     */ {
        /*     */     public static final String TEST = "https://sbcheckout.payfort.com";
        /*     */     public static final String PRODUCTION = "https://checkout.payfort.com";
/*     */
    }
/*     */
}