package ir.co.ocs.envoriment;

import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;

import java.util.Objects;

public abstract class AbstractNetworkChannel implements NetworkChannel {

    protected DefaultTcpSocketConfiguration defaultTcpSocketConfiguration;


    public void setDefaultTcpSocketConfiguration(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration) {
        if (Objects.isNull(defaultTcpSocketConfiguration))
            throw new NullPointerException("defaultTcpSocketConfiguration must not be Null");
        this.defaultTcpSocketConfiguration = defaultTcpSocketConfiguration;

    }
}
