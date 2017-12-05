/*     */
package com.payfort.fort.android.sdk.activities;
/*     */ 
/*     */

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.transition.Fade;
import android.transition.Slide;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.payfort.fort.android.sdk.R;
import com.payfort.sdk.android.dependancies.models.SdkResponse;
import com.payfort.sdk.android.dependancies.utils.Utils;
import com.shamanland.fonticon.FontIconView;


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
/*     */ public class CreditCardResponseActivity extends FortActivity
/*     */ {
    /*  30 */   private final int DISPLAY_TIME = 5000;
    /*     */   private TextView responseStatusHintTV;
    /*     */   private TextView responseInfo1TV;
    /*  25 */   private TextView responseInfo2TV = null;
    /*  26 */   private FontIconView responseStatusIconIV = null;
    /*  27 */   private RelativeLayout containerRL = null;
    /*     */
/*  29 */   private SdkResponse sdkResponse = null;

    /*     */
/*     */
    protected void onCreate(Bundle savedInstanceState)
/*     */ {
/*  34 */
        super.onCreate(savedInstanceState);
/*  35 */
        setContentView(R.layout.activity_cc_response);
/*     */
    }

    /*     */
/*     */
    private void initActivity()
/*     */ {
/*  40 */
        this.responseStatusHintTV = findViewById(R.id.responseStatusHintTV);
/*  41 */
        this.responseInfo1TV = findViewById(R.id.responseInfo1TV);
/*  42 */
        this.responseInfo2TV = findViewById(R.id.responseInfo2TV);
/*  43 */
        this.containerRL = findViewById(R.id.responseContainerRL);
/*  44 */
        this.responseStatusIconIV = findViewById(R.id.responseStatusIconIV);
/*     */
    }

    /*     */
/*     */
    private void fillViews() {
/*  48 */
        if (this.sdkResponse.isSuccess()) {
/*  49 */
            if (Build.VERSION.SDK_INT >= 21) {
/*  50 */
                this.containerRL.setBackground(getResources().getDrawable(R.color.pf_green, null));
/*     */
            } else {
/*  52 */
                this.containerRL.setBackgroundColor(getResources().getColor(R.color.pf_green));
/*     */
            }
/*  54 */
            this.responseStatusIconIV.setText(getResources().getString(R.string.icon_ok_circled));
/*  55 */
            this.responseStatusHintTV.setText(getResources().getString(R.string.pf_resp_page_great));
/*  56 */
            this.responseInfo1TV.setText(Utils.getParamValue(this.sdkResponse.getResponseMap(), "fort_id"));
/*  57 */
            this.responseInfo2TV.setText(Utils.getParamValue(this.sdkResponse.getResponseMap(), "response_message"));
/*     */
        } else {
/*  59 */
            if (Build.VERSION.SDK_INT >= 21) {
/*  60 */
                this.containerRL.setBackground(getResources().getDrawable(R.color.pf_red, null));
/*     */
            } else {
/*  62 */
                this.containerRL.setBackgroundColor(getResources().getColor(R.color.pf_red));
/*     */
            }
/*  64 */
            this.responseStatusIconIV.setText(getResources().getString(R.string.icon_cancel_circled));
/*  65 */
            this.responseStatusHintTV.setText(getResources().getString(R.string.pf_resp_page_failed));
/*  66 */
            String code = "response_code : " + Utils.getParamValue(this.sdkResponse.getResponseMap(), "response_code");
/*  67 */
            this.responseInfo1TV.setText(code);
/*  68 */
            String message = "response_message : " + Utils.getParamValue(this.sdkResponse.getResponseMap(), "response_message");
/*  69 */
            this.responseInfo2TV.setText(message);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    protected void onResume()
/*     */ {
/*  75 */
        super.onResume();
/*  76 */
        initActivity();
/*     */     
/*  78 */
        if ((this.sdkResponse == null) && (getIntent().hasExtra("sdkResp")))
/*  79 */ this.sdkResponse = ((SdkResponse) getIntent().getSerializableExtra("sdkResp"));
/*  80 */
        if (this.sdkResponse == null) {
/*  81 */
            finish();
/*     */
        } else {
/*  83 */
            fillViews();
/*     */       
/*  85 */
            new Handler().postDelayed(new Runnable()
/*     */ {
                /*     */
/*  88 */
                public void run() {
                    CreditCardResponseActivity.this.finish();
                }
            }, 5000L);
/*     */
        }
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */
    public void onBackPressed()
/*     */ {
/*  96 */
        super.onBackPressed();
/*  97 */
        finish();
/*     */
    }

    /*     */
/*     */
    public void finish()
/*     */ {
/* 102 */
        super.finish();
/* 103 */
        Intent broadcastIntent = new Intent("responseEvent");
/* 104 */
        broadcastIntent.putExtra("sdkResp", this.sdkResponse);
/* 105 */
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
/*     */
    }

    /*     */
/*     */
    @android.annotation.TargetApi(21)
/*     */ private void setupWindowAnimations() {
/* 110 */
        if (Build.VERSION.SDK_INT >= 21) {
/* 111 */
            Fade fade = new Fade();
/* 112 */
            fade.setDuration(1000L);
/* 113 */
            getWindow().setEnterTransition(fade);
/*     */       
/* 115 */
            Slide slide = new Slide();
/* 116 */
            slide.setDuration(1000L);
/* 117 */
            getWindow().setReturnTransition(slide);
/*     */
        }
/*     */
    }
/*     */
}