package ir.co.ocs.envoriment.networkchannel;

import javax.net.ssl.SSLContext;

public interface SSLManger {
    SSLContext createSSLContext() throws Exception;
}
