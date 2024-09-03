package ir.co.ocs.envoriment;

import ir.co.ocs.Handlers.NetworkChannelHandler;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationHandler;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Client extends AbstractNetworkChannel {

    NioSocketConnector client;


    public Client(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration, SocketConfiguration socketConfiguration) {
        super(defaultTcpSocketConfiguration, socketConfiguration);
    }

    public Client(IoServiceFactory ioServiceFactory,
            DefaultTcpSocketConfiguration defaultTcpSocketConfiguration) {
        super(defaultTcpSocketConfiguration, new SocketConfigurationHandler());
    }

    @Override
    public void setHandler(NetworkChannelHandler handler) {

    }

    @Override
    public void addFilter(String name, IoFilter filterChain) {

    }

    @Override
    public void addProcessor() {

    }

    @Override
    public NetworkChannel start() {
        return null;
    }

    @Override
    public NetworkChannel stop() {
        return null;
    }

    @Override
    public NetworkChannel restart() {
        return null;
    }
}
