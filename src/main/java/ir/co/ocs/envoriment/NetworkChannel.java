package ir.co.ocs.envoriment;

import ir.co.ocs.Handlers.NetworkChannelHandler;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoService;

public interface NetworkChannel {
    public void setHandler(NetworkChannelHandler handler);

    public void addFilter(String name, IoFilter filterChain);

    public void addProcessor();


    public void setDefaultHandler(IoService ioService);

}
