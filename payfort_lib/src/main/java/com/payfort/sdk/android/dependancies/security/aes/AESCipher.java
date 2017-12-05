/*     */
package com.payfort.sdk.android.dependancies.security.aes;
/*     */ 
/*     */

import com.google.common.base.Throwables;
import com.google.common.io.BaseEncoding;

import org.apache.commons.codec.binary.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
/*     */ public class AESCipher
/*     */ {
    /*     */   private static final String ALGORITHM_AES256 = "AES/CBC/PKCS5Padding";
    /*  19 */   private static final byte[] INITIAL_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    /*     */
/*     */   private final SecretKeySpec secretKeySpec;
    /*     */
/*     */   private final Cipher cipher;
    /*     */
/*     */   private IvParameterSpec iv;

    /*     */
/*     */ 
/*     */
    public AESCipher(Key key)
/*     */ {
/*  30 */
        this(key.getEncoded());
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public AESCipher(Key key, byte[] iv)
/*     */ {
/*  39 */
        this(key.getEncoded(), iv);
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public AESCipher(byte[] key)
/*     */ {
/*  50 */
        this(key, INITIAL_IV);
/*     */
    }

    /*     */
/*     */
    private AESCipher(byte[] key, byte[] iv) {
/*     */
        try {
/*  55 */
            this.secretKeySpec = new SecretKeySpec(key, "AES");
/*  56 */
            this.iv = new IvParameterSpec(iv);
/*  57 */
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*     */
        } catch (Exception e) {
/*  59 */
            throw Throwables.propagate(e);
/*     */
        }
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public String getEncryptedMessage(byte[] message)
/*     */ {
/*     */
        try
/*     */ {
/*  71 */
            Cipher cipher = getCipher(1);
/*     */       
/*  73 */
            byte[] encryptedTextBytes = cipher.doFinal(message);
/*     */       
/*  75 */
            return BaseEncoding.base64().encode(encryptedTextBytes);
/*     */
        } catch (Exception e) {
/*  77 */
            throw Throwables.propagate(e);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public String getEncryptedMessage(String message) {
/*     */
        try {
/*  83 */
            Cipher cipher = getCipher(1);
/*     */       
/*  85 */
            byte[] encryptedTextBytes = cipher.doFinal(message.getBytes("UTF-8"));
/*     */       
/*  87 */
            return BaseEncoding.base64().encode(encryptedTextBytes);
/*     */
        } catch (Exception e) {
/*  89 */
            throw Throwables.propagate(e);
/*     */
        }
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public String getDecryptedMessage(byte[] message)
/*     */ {
/*     */
        try
/*     */ {
/* 101 */
            Cipher cipher = getCipher(2);
/*     */       
/*     */ 
/* 104 */
            byte[] encryptedTextBytes = message;
/* 105 */
            byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
/*     */       
/* 107 */
            return new String(decryptedTextBytes);
/*     */
        } catch (Exception e) {
/* 109 */
            throw Throwables.propagate(e);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public String getDecryptedMessage(String message) {
/*     */
        try {
/* 115 */
            Cipher cipher = getCipher(2);
/*     */       
/* 117 */
            byte[] encryptedTextBytes = new Base64().decode(message.getBytes("utf-8"));
/* 118 */
            return new String(cipher.doFinal(encryptedTextBytes));
/*     */
        } catch (Exception e) {
/* 120 */
            throw Throwables.propagate(e);
/*     */
        }
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public String getIV()
/*     */ {
/* 130 */
        return BaseEncoding.base64().encode(this.iv.getIV());
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public String getKey()
/*     */ {
/* 139 */
        return getKey(KeyEncoding.BASE64);
/*     */
    }

    /*     */
/*     */
    public String getKey(KeyEncoding encoding) {
/* 143 */
        String result = null;
/* 144 */
        switch (encoding) {
/*     */
            case BASE64:
/* 146 */
                result = BaseEncoding.base64().encode(this.secretKeySpec.getEncoded());
/* 147 */
                break;
/*     */
            case HEX:
/* 149 */
                result = BaseEncoding.base16().encode(this.secretKeySpec.getEncoded());
/* 150 */
                break;
/*     */
            case BASE32:
/* 152 */
                result = BaseEncoding.base32().encode(this.secretKeySpec.getEncoded());
/*     */
        }
/*     */     
/*     */     
/* 156 */
        return result;
/*     */
    }

    /*     */
/*     */
    private Cipher getCipher(int encryptMode) throws InvalidKeyException, InvalidAlgorithmParameterException {
/* 160 */
        this.cipher.init(encryptMode, getSecretKeySpec(), this.iv);
/* 161 */
        return this.cipher;
/*     */
    }

    /*     */
/*     */
    private SecretKeySpec getSecretKeySpec() {
/* 165 */
        return this.secretKeySpec;
/*     */
    }
/*     */
}