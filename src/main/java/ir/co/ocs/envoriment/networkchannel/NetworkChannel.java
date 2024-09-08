package ir.co.ocs.envoriment.networkchannel;

import ir.co.ocs.Handlers.NetworkChannelHandler;
import ir.co.ocs.socketconfiguration.BaseTcpSocketConfiguration;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoService;

public interface NetworkChannel extends ServiceLifecycle {


    public void addProcessor();


    BaseTcpSocketConfiguration getConfiguration();

}
