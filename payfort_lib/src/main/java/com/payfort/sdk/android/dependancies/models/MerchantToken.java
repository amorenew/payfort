/*    */
package com.payfort.sdk.android.dependancies.models;
/*    */ 
/*    */

import java.io.Serializable;

/*    */
/*    */ 
/*    */ public class MerchantToken
/*    */ implements Serializable
/*    */ {
    /*    */   private String maskedCardNumber;
    /*    */   private String expDate;
    /*    */   private boolean rememberMe;

    /*    */
/*    */
    public String getMaskedCardNumber()
/*    */ {
/* 15 */
        return this.maskedCardNumber;
/*    */
    }

    /*    */
/* 18 */
    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    /*    */
/*    */
    public String getExpDate() {
/* 21 */
        return this.expDate;
/*    */
    }

    /*    */
/* 24 */
    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    /*    */
/*    */
    public boolean isRememberMe()
/*    */ {
/* 28 */
        return this.rememberMe;
/*    */
    }

    /*    */
/*    */
    public void setRememberMe(boolean rememberMe) {
/* 32 */
        this.rememberMe = rememberMe;
/*    */
    }
/*    */
}