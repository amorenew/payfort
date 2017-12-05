/*    */
package com.payfort.sdk.android.dependancies.models;
/*    */ 
/*    */

import java.io.Serializable;
import java.util.Map;

/*    */
/*    */ 
/*    */ 
/*    */ public class FortRequest
/*    */ implements Serializable
/*    */ {
    /*    */   private Map<String, String> requestMap;
    /*    */   private boolean showResponsePage;

    /*    */
/*    */
    public Map<String, String> getRequestMap()
/*    */ {
/* 15 */
        return this.requestMap;
/*    */
    }

    /*    */
/*    */
    public void setRequestMap(Map<String, String> requestMap) {
/* 19 */
        this.requestMap = requestMap;
/*    */
    }

    /*    */
/*    */
    public boolean isShowResponsePage() {
/* 23 */
        return this.showResponsePage;
/*    */
    }

    /*    */
/*    */
    public void setShowResponsePage(boolean showResponsePage) {
/* 27 */
        this.showResponsePage = showResponsePage;
/*    */
    }
/*    */
}