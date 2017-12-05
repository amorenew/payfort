/*    */
package com.payfort.sdk.android.dependancies.security;
/*    */ 
/*    */

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.payfort.sdk.android.dependancies.exceptions.FortException;
import com.payfort.sdk.android.dependancies.security.aes.AESCipherManager;
import com.payfort.sdk.android.dependancies.security.rsa.RSAEncryptUtil;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.spec.SecretKeySpec;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataSecurityService
/*    */ {
    /*    */   private static final String FORT_SECURITY_SEPARATOR = "___F0RT___";

    /*    */
/*    */
    public static RSAPublicKey getPublicKey(HttpsURLConnection httpsURLConnection)
/*    */     throws Exception
/*    */ {
/* 30 */
        X509Certificate certificate = null;
/* 31 */
        for (Certificate c : httpsURLConnection.getServerCertificates()) {
/* 32 */
            certificate = (X509Certificate) c;
/* 33 */
            if ((certificate.getSubjectDN().getName().contains("payfort")) || (certificate.getSubjectDN().getName().contains("PAYFORT"))) {
/*    */
                break;
/*    */
            }
/*    */
        }
/* 37 */
        if (certificate != null) {
/* 38 */
            return (RSAPublicKey) certificate.getPublicKey();
/*    */
        }
/* 40 */
        return null;
/*    */
    }

    /*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */
    public static String encryptRequestData(String jsonReqString, RSAPublicKey publicKey, SecretKeySpec secretKeySpec)
/*    */     throws FortException
/*    */ {
/*    */
        try
/*    */ {
/* 53 */
            AESCipherManager aesCipherManager = new AESCipherManager();
/* 54 */
            String encryptedAESData = aesCipherManager.encryptData(jsonReqString, secretKeySpec);
/*    */       
/* 56 */
            byte[] encryptedAESKey = RSAEncryptUtil.encrypt(secretKeySpec.getEncoded(), publicKey);
/* 57 */
            Gson gson = new Gson();
/*    */       
/* 59 */
            return encryptedAESData + "___F0RT___" + BaseEncoding.base64().encode(encryptedAESKey);
/*    */
        } catch (Exception e) {
/* 61 */
            throw new FortException("006");
/*    */
        }
/*    */
    }
/*    */
}