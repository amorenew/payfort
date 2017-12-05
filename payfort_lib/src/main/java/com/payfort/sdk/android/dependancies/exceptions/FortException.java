/*    */
package com.payfort.sdk.android.dependancies.exceptions;

/*    */
/*    */ public class FortException
/*    */ extends Exception
/*    */ {
    /*    */   private String code;

    /*    */
/*    */
    public FortException(String code)
/*    */ {
/* 10 */
        this.code = code;
/*    */
    }

    /*    */
/*    */
    public String getCode() {
/* 14 */
        return this.code;
/*    */
    }
/*    */
}