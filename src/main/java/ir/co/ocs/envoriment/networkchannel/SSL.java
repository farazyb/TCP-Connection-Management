package ir.co.ocs.envoriment.networkchannel;

import org.apache.mina.filter.ssl.SslFilter;

import javax.net.ssl.SSLContext;

public interface SSL {
    SSLContext addSSL(String keystorePath, String password);
}
