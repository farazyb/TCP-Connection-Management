package ir.co.ocs.envoriment;

import ir.co.ocs.Handlers.NetworkChannelHandler;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoService;

public interface NetworkChannel {
    public IoService create();

    public NetworkChannel start(int port);

    public NetworkChannel stop();

    public NetworkChannel restart();

    public void setHandler(NetworkChannelHandler handler);

    public void addFilter(String name, IoFilter filterChain);

    public void addProcessor();

    public void setConfig(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration);

}
