package ir.co.ocs.Envoriment;

import ir.co.ocs.Handlers.NetWorkChannelHandler;
import org.apache.mina.core.filterchain.IoFilter;

public interface NetworkChannel {

    public NetworkChannel start(int port);

    public NetworkChannel stop();

    public NetworkChannel restart();

    public void setHandler(NetWorkChannelHandler handler);

    public void addFilter(String name, IoFilter filterChain);

    public void create();

    public void addProcessor();
}
