/*    */
package com.payfort.sdk.android.dependancies.connection;
/*    */ 
/*    */

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

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
/*    */ public class TLSSocketFactory
/*    */ extends SSLSocketFactory
/*    */ {
    /*    */   private SSLSocketFactory internalSSLSocketFactory;

    /*    */
/*    */
    public TLSSocketFactory()
/*    */     throws KeyManagementException, NoSuchAlgorithmException
/*    */ {
/* 31 */
        SSLContext context = SSLContext.getInstance("TLS");
/* 32 */
        context.init(null, null, null);
/* 33 */
        this.internalSSLSocketFactory = context.getSocketFactory();
/*    */
    }

    /*    */
/*    */
    public TLSSocketFactory(TrustManager[] trustManagers) throws KeyManagementException, NoSuchAlgorithmException {
/* 37 */
        SSLContext context = SSLContext.getInstance("TLS");
/* 38 */
        context.init(null, trustManagers, null);
/* 39 */
        this.internalSSLSocketFactory = context.getSocketFactory();
/*    */
    }

    /*    */
/*    */
    public TLSSocketFactory(TrustManager[] trustManagers, SecureRandom secureRandom) throws KeyManagementException, NoSuchAlgorithmException {
/* 43 */
        SSLContext context = SSLContext.getInstance("TLS");
/* 44 */
        context.init(null, trustManagers, secureRandom);
/* 45 */
        this.internalSSLSocketFactory = context.getSocketFactory();
/*    */
    }

    /*    */
/*    */
    public String[] getDefaultCipherSuites()
/*    */ {
/* 50 */
        return this.internalSSLSocketFactory.getDefaultCipherSuites();
/*    */
    }

    /*    */
/*    */
    public String[] getSupportedCipherSuites()
/*    */ {
/* 55 */
        return this.internalSSLSocketFactory.getSupportedCipherSuites();
/*    */
    }

    /*    */
/*    */
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException
/*    */ {
/* 60 */
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(s, host, port, autoClose));
/*    */
    }

    /*    */
/*    */
    public Socket createSocket(String host, int port) throws IOException
/*    */ {
/* 65 */
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(host, port));
/*    */
    }

    /*    */
/*    */
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException
/*    */ {
/* 70 */
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(host, port, localHost, localPort));
/*    */
    }

    /*    */
/*    */
    public Socket createSocket(InetAddress host, int port) throws IOException
/*    */ {
/* 75 */
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(host, port));
/*    */
    }

    /*    */
/*    */
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException
/*    */ {
/* 80 */
        return enableTLSOnSocket(this.internalSSLSocketFactory.createSocket(address, port, localAddress, localPort));
/*    */
    }

    /*    */
/*    */
    private Socket enableTLSOnSocket(Socket socket) {
/* 84 */
        if ((socket != null) && ((socket instanceof SSLSocket)))
/*    */ {
/* 86 */
            ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.1", "TLSv1.2"});
/*    */       
/* 88 */
            List<String> enabledProtocols = new ArrayList(Arrays.asList(((SSLSocket) socket).getEnabledProtocols()));
/* 89 */
            if (enabledProtocols.size() > 1) {
/* 90 */
                enabledProtocols.remove("SSLv3");
/*    */
            }
/* 92 */
            ((SSLSocket) socket).setEnabledProtocols(enabledProtocols.toArray(new String[enabledProtocols.size()]));
/*    */
        }
/* 94 */
        return socket;
/*    */
    }
/*    */
}