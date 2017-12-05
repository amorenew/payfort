/*     */
package com.payfort.sdk.android.dependancies.security.rsa;
/*     */ 
/*     */

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

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
/*     */ public class RSAEncryptUtil
/*     */ {
    /*     */   protected static final String ALGORITHM = "RSA";

    /*     */
/*     */
    public RSAEncryptUtil()
/*     */ {
/*  42 */
        init();
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static void init()
/*     */ {
/*  50 */
        Security.addProvider(new BouncyCastleProvider());
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static KeyPair generateKey()
/*     */     throws NoSuchAlgorithmException
/*     */ {
/*  60 */
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
/*  61 */
        keyGen.initialize(1024);
/*  62 */
        KeyPair key = keyGen.generateKeyPair();
/*  63 */
        return key;
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
/*     */
    public static byte[] encrypt(byte[] text, PublicKey key)
/*     */     throws Exception
/*     */ {
/*  76 */
        byte[] cipherText = null;
/*     */     
/*     */ 
/*  79 */
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
/*     */     
/*     */ 
/*  82 */
        cipher.init(1, key);
/*  83 */
        cipherText = cipher.doFinal(text);
/*  84 */
        return cipherText;
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
/*     */
    public static String encrypt(String text, PublicKey key)
/*     */     throws Exception
/*     */ {
/*  97 */
        byte[] cipherText = encrypt(text.getBytes("UTF8"), key);
/*  98 */
        String encryptedText = encodeBASE64(cipherText);
/*  99 */
        return encryptedText;
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
    public static byte[] decrypt(byte[] text, PrivateKey key)
/*     */     throws Exception
/*     */ {
/* 111 */
        byte[] dectyptedText = null;
/*     */     
/*     */ 
/*     */ 
/* 115 */
        Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding");
/* 116 */
        cipher.init(2, key);
/* 117 */
        dectyptedText = cipher.doFinal(text);
/* 118 */
        return dectyptedText;
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
/*     */ 
/*     */ 
/*     */
    public static String decrypt(String text, PrivateKey key)
/*     */     throws Exception
/*     */ {
/* 133 */
        byte[] dectyptedText = decrypt(decodeBASE64(text), key);
/* 134 */
        String result = new String(dectyptedText, "UTF8");
/* 135 */
        return result;
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
/*     */
    public static String getKeyAsString(Key key)
/*     */ {
/* 147 */
        byte[] keyBytes = key.getEncoded();
/* 148 */
        return encodeBASE64(keyBytes);
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static PrivateKey getPrivateKeyFromString(String key)
/*     */     throws Exception
/*     */ {
/* 159 */
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
/* 160 */
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodeBASE64(key));
/* 161 */
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
/* 162 */
        return privateKey;
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static PublicKey getPublicKeyFromString(String key)
/*     */     throws Exception
/*     */ {
/* 173 */
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
/* 174 */
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodeBASE64(key));
/* 175 */
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
/* 176 */
        return publicKey;
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
    private static String encodeBASE64(byte[] bytes)
/*     */ {
/* 187 */
        return new String(Base64.encodeBase64(bytes));
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
    private static byte[] decodeBASE64(String text)
/*     */     throws IOException
/*     */ {
/* 199 */
        return Base64.decodeBase64(text.getBytes());
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
/*     */
    public static void encryptFile(String srcFileName, String destFileName, PublicKey key)
/*     */     throws Exception
/*     */ {
/* 212 */
        encryptDecryptFile(srcFileName, destFileName, key, 1);
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
/*     */
    public static void decryptFile(String srcFileName, String destFileName, PrivateKey key)
/*     */     throws Exception
/*     */ {
/* 225 */
        encryptDecryptFile(srcFileName, destFileName, key, 2);
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
/*     */ 
/*     */
    public static void encryptDecryptFile(String srcFileName, String destFileName, Key key, int cipherMode)
/*     */     throws Exception
/*     */ {
/* 239 */
        OutputStream outputWriter = null;
/* 240 */
        InputStream inputReader = null;
/*     */
        try
/*     */ {
/* 243 */
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
/* 244 */
            String textLine = null;
/*     */       
/*     */ 
/*     */ 
/* 248 */
            byte[] buf = cipherMode == 1 ? new byte[100] : new byte[''];
/*     */       
/*     */ 
/* 251 */
            cipher.init(cipherMode, key);
/*     */       
/*     */ 
/* 254 */
            outputWriter = new FileOutputStream(destFileName);
/* 255 */
            inputReader = new FileInputStream(srcFileName);
/* 256 */
            int bufl;
            while ((bufl = inputReader.read(buf)) != -1)
/*     */ {
/* 258 */
                byte[] encText = null;
/* 259 */
                if (cipherMode == 1)
/*     */ {
/* 261 */
                    encText = encrypt(copyBytes(buf, bufl), (PublicKey) key);
/*     */
                }
/*     */
                else
/*     */ {
/* 265 */
                    encText = decrypt(copyBytes(buf, bufl), (PrivateKey) key);
/*     */
                }
/* 267 */
                outputWriter.write(encText);
/*     */
            }
/* 269 */
            outputWriter.flush();
            return;
/*     */ 
/*     */
        }
/*     */ finally
/*     */ {
/*     */
            try
/*     */ {
/* 276 */
                if (outputWriter != null)
/*     */ {
/* 278 */
                    outputWriter.close();
/*     */
                }
/* 280 */
                if (inputReader != null)
/*     */ {
/* 282 */
                    inputReader.close();
/*     */
                }
/*     */
            }
/*     */ catch (Exception localException1) {
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static byte[] copyBytes(byte[] arr, int length)
/*     */ {
/* 294 */
        byte[] newArr = null;
/* 295 */
        if (arr.length == length)
/*     */ {
/* 297 */
            newArr = arr;
/*     */
        }
/*     */
        else
/*     */ {
/* 301 */
            newArr = new byte[length];
/* 302 */
            for (int i = 0; i < length; i++)
/*     */ {
/* 304 */
                newArr[i] = arr[i];
/*     */
            }
/*     */
        }
/* 307 */
        return newArr;
/*     */
    }

    /*     */
/*     */
    public static KeyStore loadKeyStore(File keystoreFile, String password, String keyStoreType)
/*     */     throws Exception
/*     */ {
/* 313 */
        if (null == keystoreFile) {
/* 314 */
            throw new IllegalArgumentException("Keystore url may not be null");
/*     */
        }
/*     */     
/* 317 */
        URI keystoreUri = keystoreFile.toURI();
/* 318 */
        URL keystoreUrl = keystoreUri.toURL();
/* 319 */
        KeyStore keystore = KeyStore.getInstance(keyStoreType);
/* 320 */
        InputStream is = null;
/*     */
        try {
/* 322 */
            is = keystoreUrl.openStream();
/* 323 */
            keystore.load(is, null == password ? null : password.toCharArray());
/*     */
        }
/*     */ finally {
/* 326 */
            if (null != is) {
/* 327 */
                is.close();
/*     */
            }
/*     */
        }
/* 330 */
        return keystore;
/*     */
    }

    /*     */
/*     */
    public static KeyPair getKeyPair(KeyStore keystore, String alias, String password) throws Exception
/*     */ {
/* 335 */
        Key key = keystore.getKey(alias, password.toCharArray());
/*     */     
/* 337 */
        Certificate cert = keystore.getCertificate(alias);
/* 338 */
        PublicKey publicKey = cert.getPublicKey();
/*     */     
/* 340 */
        return new KeyPair(publicKey, (PrivateKey) key);
/*     */
    }
/*     */
}