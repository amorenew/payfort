/*     */
package com.payfort.fort.android.sdk.activities;
/*     */ 
/*     */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.payfort.sdk.android.dependancies.utils.Utils;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import java.util.HashMap;
import java.util.Locale;

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
/*     */ public class FortActivity
/*     */ extends AppCompatActivity
/*     */ {
    /*     */   protected FortRequest merchantFortRequest;
    /*  24 */   protected String systemDefaultLanguage = null;

    /*     */
/*     */
    protected void onCreate(Bundle savedInstanceState)
/*     */ {
/*  28 */
        super.onCreate(savedInstanceState);
/*     */     
/*  30 */
        FontIconTypefaceHolder.init(getResources().getAssets(), "fontello.ttf");
/*     */     
/*     */ 
/*  33 */
        initMerchantRequestObj();
/*     */     
/*     */ 
/*  36 */
        initDefaultLocale();
/*     */     
/*     */ 
/*  39 */
        updateByLanguage(this, Utils.getLanguage(this.merchantFortRequest.getRequestMap()));
/*     */
    }

    /*     */
/*     */
    public void finish()
/*     */ {
/*  44 */
        super.finish();
/*  45 */
        restoreLocale();
/*     */
    }

    /*     */
/*     */
    private void initDefaultLocale() {
/*  49 */
        if (getIntent().hasExtra("defaultLocale")) {
/*  50 */
            this.systemDefaultLanguage = getIntent().getExtras().getString("defaultLocale");
/*     */
        }
/*     */
    }

    /*     */
/*     */
    private Locale updateByLanguage(Context context, String language)
/*     */ {
/*  56 */
        Configuration config = context.getResources().getConfiguration();
/*     */
        Locale sysLocale;
/*  58 */
        if (Build.VERSION.SDK_INT >= 24) {
/*  59 */
            sysLocale = getSystemLocale(config);
/*     */
        } else {
/*  61 */
            sysLocale = getSystemLocaleLegacy(config);
/*     */
        }
/*     */     
/*  64 */
        if (this.systemDefaultLanguage == null) {
/*  65 */
            this.systemDefaultLanguage = sysLocale.getLanguage();
/*     */
        }
/*  67 */
        if ((!language.equals("")) && (!sysLocale.getLanguage().equals(language))) {
/*  68 */
            Locale locale = new Locale(language);
/*  69 */
            Locale.setDefault(locale);
/*  70 */
            if (Build.VERSION.SDK_INT >= 24) {
/*  71 */
                setSystemLocale(config, locale);
/*     */
            } else {
/*  73 */
                setSystemLocaleLegacy(config, locale);
/*     */
            }
/*     */       
/*  76 */
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
/*     */
        }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */
        return sysLocale;
/*     */
    }

    /*     */
/*     */
    private void restoreLocale() {
/*  87 */
        if (this.systemDefaultLanguage != null) {
/*  88 */
            updateByLanguage(this, this.systemDefaultLanguage);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    private Locale getSystemLocaleLegacy(Configuration config) {
/*  93 */
        return config.locale;
/*     */
    }

    /*     */
/*     */
    @TargetApi(24)
/*     */ private Locale getSystemLocale(Configuration config) {
/*  98 */
        return config.getLocales().get(0);
/*     */
    }

    /*     */
/*     */
    private void setSystemLocaleLegacy(Configuration config, Locale locale) {
/* 102 */
        config.locale = locale;
/* 103 */
        if (Build.VERSION.SDK_INT >= 17) {
/* 104 */
            config.setLayoutDirection(locale);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    @TargetApi(24)
/*     */ private void setSystemLocale(Configuration config, Locale locale) {
/* 110 */
        config.setLocale(locale);
/* 111 */
        config.setLayoutDirection(locale);
/*     */
    }

    /*     */
/*     */
    protected void onResume()
/*     */ {
/* 116 */
        super.onResume();
/* 117 */
        if (this.merchantFortRequest == null) {
/* 118 */
            initMerchantRequestObj();
/*     */
        }
/*     */
    }

    /*     */
/*     */
    private void initMerchantRequestObj() {
/* 123 */
        if (getIntent().hasExtra("merchantReq"))
/* 124 */
            this.merchantFortRequest = ((FortRequest) getIntent().getSerializableExtra("merchantReq"));
/* 125 */
        if (this.merchantFortRequest == null)
/*     */ {
/* 127 */
            this.merchantFortRequest = new FortRequest();
/* 128 */
            this.merchantFortRequest.setRequestMap(new HashMap());
/*     */
        }
/*     */
    }

    /*     */
/*     */
    protected String getEnvironment()
/*     */ {
/* 134 */
        if (getIntent().hasExtra("environment")) {
/* 135 */
            return getIntent().getStringExtra("environment");
/*     */
        }
/* 137 */
        return null;
/*     */
    }
/*     */
}