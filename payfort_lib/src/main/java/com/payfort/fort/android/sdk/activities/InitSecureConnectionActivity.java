/*     */
package com.payfort.fort.android.sdk.activities;
/*     */ 
/*     */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.payfort.fort.android.sdk.R;
import com.payfort.fort.android.sdk.base.FortSdk;
import com.payfort.fort.android.sdk.base.SdkUtils;
import com.payfort.sdk.android.dependancies.connection.ConnectionAdapter;
import com.payfort.sdk.android.dependancies.exceptions.FortException;
import com.payfort.sdk.android.dependancies.models.SdkRequest;
import com.payfort.sdk.android.dependancies.models.SdkResponse;
import com.payfort.sdk.android.dependancies.security.DataSecurityService;
import com.payfort.sdk.android.dependancies.security.aes.AESCipherManager;
import com.victor.loading.rotate.RotateLoading;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

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
/*     */ public class InitSecureConnectionActivity
/*     */ extends FortActivity
/*     */ {
    /*  43 */   private RotateLoading newtonCradleLoading = null;
    /*  44 */   private BroadcastReceiver mMessageReceiver = null;
    /*  45 */   private RelativeLayout containerRL = null;
    /*  46 */   private Handshake handshake = null;

    /*     */
/*     */
    protected void onCreate(Bundle savedInstanceState)
/*     */ {
/*  50 */
        super.onCreate(savedInstanceState);
/*  51 */
        setContentView(R.layout.activity_init_secure_conn);
/*  52 */
        setFinishOnTouchOutside(false);
/*  53 */
        initActivity();
/*  54 */
        handleShowLoading();
/*  55 */
        setupLocalBroadCastConfigs();
/*     */
    }

    /*     */
/*     */
    private void handleShowLoading() {
/*  59 */
        if ((getIntent().hasExtra("showLoading")) && (!getIntent().getExtras().getBoolean("showLoading", true))) {
/*  60 */
            this.containerRL.setVisibility(View.GONE);
/*     */
        } else {
/*  62 */
            this.newtonCradleLoading.start();
/*     */
        }
/*     */
    }

    /*     */
/*     */
    protected void onResume()
/*     */ {
/*  68 */
        super.onResume();
/*  69 */
        if (this.handshake == null) {
/*  70 */
            if (SdkUtils.haveNetworkConnection(this)) {
/*  71 */
                this.handshake = new Handshake();
/*  72 */
                this.handshake.execute();
/*     */
            } else {
/*  74 */
                Toast.makeText(this, getResources().getString(R.string.pf_no_connection), Toast.LENGTH_LONG).show();
/*  75 */
                SdkResponse sdkResponse = SdkUtils.collectResponse(this, null, this.merchantFortRequest.getRequestMap());
/*  76 */
                Intent intent = getIntent();
/*  77 */
                intent.putExtra("sdkResp", sdkResponse);
/*  78 */
                setResult(-1, intent);
/*  79 */
                finish();
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */
    private void initActivity() {
/*  85 */
        this.newtonCradleLoading = findViewById(R.id.newton_cradle_loading);
/*  86 */
        this.containerRL = findViewById(R.id.initContainerRL);
/*     */
    }

    /*     */
/*     */
    public void onBackPressed()
/*     */ {
/*  91 */
        super.onBackPressed();
/*     */
    }

    /*     */
/*     */
    private void setupLocalBroadCastConfigs() {
/* 191 */
        this.mMessageReceiver = new BroadcastReceiver()
/*     */ {
            /*     */
            public void onReceive(Context context, Intent intent) {
/* 194 */
                Intent returnIntent = InitSecureConnectionActivity.this.getIntent();
/* 195 */
                if (intent.hasExtra("sdkResp")) {
/* 196 */
                    returnIntent.putExtra("sdkResp", intent.getSerializableExtra("sdkResp"));
/*     */
                }
/* 198 */
                InitSecureConnectionActivity.this.setResult(-1, returnIntent);
/* 199 */
                InitSecureConnectionActivity.this.finish();
/*     */
            }
/* 201 */
        };
/* 202 */
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mMessageReceiver, new IntentFilter("responseEvent"));
/*     */
    }

    /*     */
/*     */
/*     */
    protected void onDestroy()
/*     */ {
/* 208 */
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mMessageReceiver);
/* 209 */
        super.onDestroy();
/*     */
    }

    /*     */
/*     */
    protected void onPause()
/*     */ {
/* 214 */
        super.onPause();
/* 215 */
        hideActivity(this.containerRL);
/*     */
    }

    /*     */
/*     */
    private void hideActivity(ViewGroup viewGroup) {
/* 219 */
        for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
/* 220 */
            View child = viewGroup.getChildAt(i);
/* 221 */
            if ((child instanceof ViewGroup)) {
/* 222 */
                hideActivity((ViewGroup) child);
/*     */
            }
/* 224 */
            else if (child != null) {
/* 225 */
                child.setVisibility(View.GONE);
/* 226 */
                viewGroup.setVisibility(View.GONE);
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */   class Handshake extends AsyncTask<String, Void, String>
/*     */ {
        /*  96 */     private Gson gson = new Gson();
        /*  97 */     private SecretKeySpec secretKeySpec = null;
        /*  98 */     private AESCipherManager aesCipherManager = null;
        /*  99 */     private String environment = null;

        /*     */
/*     */     Handshake() {
        }

        /*     */
/* 103 */
        protected void onPreExecute() {
            super.onPreExecute();
/* 104 */
            this.aesCipherManager = new AESCipherManager();
/* 105 */
            this.environment = InitSecureConnectionActivity.this.getEnvironment();
/*     */
        }

        /*     */
/*     */
        protected String doInBackground(String... params)
/*     */ {
/*     */
            try {
/* 111 */
                if (this.environment == null) {
/* 112 */
                    throw new FortException("006");
/*     */
                }
/* 114 */
                HttpsURLConnection urlConnection = new ConnectionAdapter().setupConnection(this.environment, "/FortAPI/sdk/validate");
/* 115 */
                urlConnection.connect();
/* 116 */
                RSAPublicKey pk = DataSecurityService.getPublicKey(urlConnection);
/* 117 */
                if (pk == null) {
/* 118 */
                    return null;
/*     */
                }
/* 120 */
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
/*     */
/* 122 */
                SdkRequest sdkRequest = new SdkRequest();
/* 123 */
                sdkRequest.setDeviceOS(SdkUtils.getOsDetails());
/* 124 */
                sdkRequest.setDeviceId(FortSdk.getDeviceId(InitSecureConnectionActivity.this));
/* 125 */
                sdkRequest.setRequestMap(InitSecureConnectionActivity.this.merchantFortRequest.getRequestMap());
/*     */
/* 127 */
                this.secretKeySpec = this.aesCipherManager.generateAESKey();
/* 128 */
                String encryptedData = DataSecurityService.encryptRequestData(this.gson.toJson(sdkRequest, SdkRequest.class), pk, this.secretKeySpec);
/* 129 */
                out.write(encryptedData);
/* 130 */
                out.close();
/*     */
/* 132 */
                StringBuilder sb = new StringBuilder();
/* 133 */
                int HttpResult = urlConnection.getResponseCode();
/* 134 */
                if (HttpResult == 200)
/*     */ {
/* 136 */
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
/* 137 */
                    String line = null;
/* 138 */
                    while ((line = br.readLine()) != null) {
/* 139 */
                        sb.append(line + "\n");
/*     */
                    }
/* 141 */
                    br.close();
/*     */
                }
/* 143 */
                urlConnection.disconnect();
/* 144 */
                return "" + sb.toString();
/*     */
            }
/*     */ catch (FortException fExc)
/*     */ {
/* 148 */
                SdkResponse sdkResponse = new SdkResponse();
/* 149 */
                Map<String, String> responseMap = new HashMap();
/* 150 */
                responseMap.put("status", "00");
/* 151 */
                responseMap.put("response_code", "00" + fExc.getCode());
/* 152 */
                responseMap.put("response_message", InitSecureConnectionActivity.this.getResources().getString(R.string.pf_technical_problem));
/* 153 */
                sdkResponse.setSuccess(false);
/* 154 */
                sdkResponse.setResponseMap(responseMap);
/* 155 */
                return this.gson.toJson(sdkResponse);
/*     */
            } catch (Exception e) {
/* 157 */
                Log.e("FortSdk", "could not connect to server", e);
/*     */
            }
/* 159 */
            return null;
/*     */
        }

        /*     */
/*     */
        protected void onPostExecute(String fortResponse)
/*     */ {
/* 164 */
            super.onPostExecute(fortResponse);
/* 165 */
            fortResponse = this.aesCipherManager.decryptMsg(fortResponse, this.secretKeySpec);
/* 166 */
            SdkResponse sdkResponse = SdkUtils.collectResponse(InitSecureConnectionActivity.this, fortResponse, InitSecureConnectionActivity.this.merchantFortRequest.getRequestMap());
/* 167 */
            if ((fortResponse != null) &&
/* 168 */         (sdkResponse.isSuccess())) {
/* 169 */
                Intent intent = new Intent(InitSecureConnectionActivity.this, CreditCardPaymentActivity.class);
/* 170 */
                intent.putExtra("merchantReq", InitSecureConnectionActivity.this.merchantFortRequest);
/* 171 */
                intent.putExtra("currencyDecimalPoints", sdkResponse.getCurrencyDecimalPoints());
/* 172 */
                if (sdkResponse.getMerchantToken() != null) {
/* 173 */
                    intent.putExtra("merchantToken", sdkResponse.getMerchantToken());
/*     */
                }
/* 175 */
                intent.putExtra("environment", this.environment);
/* 176 */
                intent.putExtra("defaultLocale", InitSecureConnectionActivity.this.systemDefaultLanguage);
/* 177 */
                InitSecureConnectionActivity.this.startActivity(intent);
/* 178 */
                return;
/*     */
            }
/*     */
/*     */
/*     */
/* 183 */
            Intent intent = InitSecureConnectionActivity.this.getIntent();
/* 184 */
            intent.putExtra("sdkResp", sdkResponse);
/* 185 */
            InitSecureConnectionActivity.this.setResult(-1, intent);
/* 186 */
            InitSecureConnectionActivity.this.finish();
/*     */
        }
/*     */
    }
/*     */
}