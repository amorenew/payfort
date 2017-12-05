/*    */
package com.payfort.sdk.android.dependancies.connection;
/*    */ 
/*    */

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
/*    */ public class ConnectionAdapter
/*    */ {
    /*    */   public static final String ENVIRONMENT_SANDBOX = "https://sbcheckout.payfort.com";
    /*    */   public static final String ENVIRONMENT_PRODUCTION = "https://checkout.payfort.com";

    /*    */
/*    */
    public HttpsURLConnection setupConnection(String environment, String URI)
/*    */     throws Exception
/*    */ {
/* 23 */
        TLSSocketFactory tlsSocketFactory = null;
/* 24 */
        URL url = new URL(environment + URI);
/* 25 */
        tlsSocketFactory = new TLSSocketFactory();
/*    */     
/* 27 */
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
/* 28 */
        urlConnection.setSSLSocketFactory(tlsSocketFactory);
/*    */     
/* 30 */
        urlConnection.setDoOutput(true);
/* 31 */
        urlConnection.setConnectTimeout(30000);
/* 32 */
        urlConnection.setRequestProperty("Content-Type", "application/json");
/* 33 */
        urlConnection.setRequestProperty("Accept", "application/json");
/* 34 */
        urlConnection.setRequestMethod("POST");
/* 35 */
        urlConnection.setRequestProperty("charset", "utf-8");
/* 36 */
        urlConnection.setUseCaches(false);
/* 37 */
        return urlConnection;
/*    */
    }
/*    */
}