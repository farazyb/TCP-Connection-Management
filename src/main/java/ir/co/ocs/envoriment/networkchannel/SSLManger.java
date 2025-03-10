package ir.co.ocs.envoriment.networkchannel;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * SSL Manager class for handling SSL/TLS configurations and certificate management.
 * This class provides functionality for creating SSL contexts and socket factories
 * with custom trust managers.
 *
 * @author OCS Team
 * @version 1.0
 */
public class SSLManger {
    private static final String SSL_PROTOCOL = "TLS";
    private static SSLManger instance;
    private SSLContext sslContext;
    private SSLSocketFactory sslSocketFactory;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private SSLManger() {
        initializeSSL();
    }

    /**
     * Gets the singleton instance of SSLManger.
     *
     * @return The SSLManger instance
     */
    public static synchronized SSLManger getInstance() {
        if (instance == null) {
            instance = new SSLManger();
        }
        return instance;
    }

    /**
     * Initializes the SSL context with a custom trust manager.
     * This implementation uses a trust-all manager for development purposes.
     * In production, you should use proper certificate validation.
     */
    private void initializeSSL() {
        try {
            sslContext = SSLContext.getInstance(SSL_PROTOCOL);
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to initialize SSL context", e);
        }
    }

    /**
     * Gets the SSL socket factory.
     *
     * @return The SSL socket factory
     */
    public SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }

    /**
     * Gets the SSL context.
     *
     * @return The SSL context
     */
    public SSLContext getSSLContext() {
        return sslContext;
    }
}
