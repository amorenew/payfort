/*    */
package com.payfort.sdk.android.dependancies.security.aes;
/*    */ 
/*    */

import com.payfort.sdk.android.dependancies.exceptions.FortException;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AESCipherManager
/*    */ {
    /* 14 */   private final String AES_ALGORITHM = "AES";

    /*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */
    public String encryptData(String msg, Key key)
/*    */ {
/* 22 */
        Key keyFromKeyStore = key;
/* 23 */
        AESCipher cipher = new AESCipher(keyFromKeyStore);
/* 24 */
        return cipher.getEncryptedMessage(msg);
/*    */
    }

    /*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */
    public String decryptMsg(String msg, Key key)
/*    */ {
/*    */
        try
/*    */ {
/* 35 */
            Key keyFromKeyStore = key;
/* 36 */
            AESCipher cipher = new AESCipher(keyFromKeyStore);
/* 37 */
            return cipher.getDecryptedMessage(msg);
/*    */
        } catch (Exception e) {
/* 39 */
            e.printStackTrace();
        }
/* 40 */
        return msg;
/*    */
    }

    /*    */
/*    */ 
/*    */ 
/*    */
    public SecretKeySpec generateAESKey()
/*    */     throws FortException
/*    */ {
/*    */
        try
/*    */ {
/* 50 */
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
/* 51 */
            sr.setSeed("23Feb2016".getBytes());
/* 52 */
            KeyGenerator kg = KeyGenerator.getInstance("AES");
/* 53 */
            kg.init(256, sr);
/* 54 */
            return new SecretKeySpec(kg.generateKey().getEncoded(), "AES");
/*    */
        } catch (Exception e) {
/* 56 */
            throw new FortException("006");
/*    */
        }
/*    */
    }
/*    */
}