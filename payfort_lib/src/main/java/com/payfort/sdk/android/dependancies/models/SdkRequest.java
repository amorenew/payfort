/*    */
package com.payfort.sdk.android.dependancies.models;
/*    */ 
/*    */

import java.util.Map;

/*    */
/*    */ public class SdkRequest
/*    */ {
    /*    */   private Map<String, String> requestMap;
    /*    */   private String deviceId;
    /*    */   private String deviceOS;

    /*    */
/*    */
    public String getDeviceOS() {
/* 12 */
        return this.deviceOS;
/*    */
    }

    /*    */
/* 15 */
    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    /*    */
/*    */
    public String getDeviceId()
/*    */ {
/* 19 */
        return this.deviceId;
/*    */
    }

    /*    */
/*    */
    public void setDeviceId(String deviceId) {
/* 23 */
        this.deviceId = deviceId;
/*    */
    }

    /*    */
/*    */
    public Map<String, String> getRequestMap() {
/* 27 */
        return this.requestMap;
/*    */
    }

    /*    */
/* 30 */
    public void setRequestMap(Map<String, String> requestMap) {
        this.requestMap = requestMap;
    }
/*    */
}