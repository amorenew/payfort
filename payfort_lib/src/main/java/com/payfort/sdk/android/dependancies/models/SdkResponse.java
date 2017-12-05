/*    */
package com.payfort.sdk.android.dependancies.models;
/*    */ 
/*    */

import java.io.Serializable;
import java.util.Map;

/*    */
/*    */ 
/*    */ public class SdkResponse implements Serializable
/*    */ {
    /*    */   private Map<String, String> responseMap;
    /*    */   private boolean isSuccess;
    /*    */   private String checker3DsURL;
    /*    */   private int currencyDecimalPoints;
    /*    */   private MerchantToken merchantToken;

    /*    */
/*    */
    public Map<String, String> getResponseMap()
/*    */ {
/* 16 */
        if (this.responseMap == null)
/* 17 */ return new java.util.HashMap();
/* 18 */
        return this.responseMap;
/*    */
    }

    /*    */
/* 21 */
    public void setResponseMap(Map<String, String> responseMap) {
        this.responseMap = responseMap;
    }

    /*    */
/*    */
    public boolean isSuccess() {
/* 24 */
        return this.isSuccess;
/*    */
    }

    /*    */
/*    */
    public void setSuccess(boolean isSuccess) {
/* 28 */
        this.isSuccess = isSuccess;
/*    */
    }

    /*    */
/* 31 */
    public String getChecker3DsURL() {
        return this.checker3DsURL;
    }

    /*    */
/* 33 */
    public void setChecker3DsURL(String checker3DsURL) {
        this.checker3DsURL = checker3DsURL;
    }

    /*    */
/*    */
    public int getCurrencyDecimalPoints() {
/* 36 */
        return this.currencyDecimalPoints;
/*    */
    }

    /*    */
/*    */
    public void setCurrencyDecimalPoints(int currencyDecimalPoints) {
/* 40 */
        this.currencyDecimalPoints = currencyDecimalPoints;
/*    */
    }

    /*    */
/*    */
    public MerchantToken getMerchantToken() {
/* 44 */
        return this.merchantToken;
/*    */
    }

    /*    */
/*    */
    public void setMerchantToken(MerchantToken merchantToken) {
/* 48 */
        this.merchantToken = merchantToken;
/*    */
    }
/*    */
}