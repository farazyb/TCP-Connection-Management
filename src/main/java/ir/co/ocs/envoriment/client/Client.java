package ir.co.ocs.envoriment.client;


import ir.co.ocs.envoriment.networkchannel.AbstractNetworkChannel;
import ir.co.ocs.envoriment.networkchannel.NetworkChannel;
import ir.co.ocs.socketconfiguration.ClientSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfiguration;
import ir.co.ocs.statistics.Statistics;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class Client extends AbstractNetworkChannel {
    private ConnectFuture future;
    private IoSession session;

    protected Client(ClientSocketConfiguration clientSocketConfiguration, NioSocketConnector connector, SocketConfiguration socketConfiguration, Statistics statistics) {
        super(clientSocketConfiguration, connector, socketConfiguration, statistics);
    }

    protected Client(ClientSocketConfiguration clientSocketConfiguration, NioSocketConnector connector) {
        super(clientSocketConfiguration, connector);
    }

    @Override
    public void addProcessor() {

    }

    @Override
    public NetworkChannel start() throws RuntimeIoException {

        this.future = nioSocketConnector().connect(new InetSocketAddress(getClientConfig().getHost(), getClientConfig().getPort()));
        this.future.awaitUninterruptibly();
        session = future.getSession();
        return this;
    }

    @Override
    public NetworkChannel stop() {
        if (session != null && session.isConnected()) {
            session.closeNow();
        }
        nioSocketConnector().dispose();
        return this;
    }


    @Override
    public NetworkChannel restart() throws RuntimeIoException {
        stop();
        return start();
    }


    protected NioSocketConnector nioSocketConnector() {
        return (NioSocketConnector) ioService;
    }

    protected ClientSocketConfiguration getClientConfig() {
        return (ClientSocketConfiguration) getConfiguration();
    }

}
