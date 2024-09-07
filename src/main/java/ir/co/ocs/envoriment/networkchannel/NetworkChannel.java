package ir.co.ocs.envoriment.networkchannel;

import ir.co.ocs.Handlers.NetworkChannelHandler;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoService;

public interface NetworkChannel extends ServiceLifecycle {

    public void setHandler(NetworkChannelHandler handler);

    public void addFilter(String name, IoFilter filterChain);

    public void addProcessor();


    public void setDefaultFilter(IoService ioService);

    DefaultTcpSocketConfiguration getConfiguration();

}
