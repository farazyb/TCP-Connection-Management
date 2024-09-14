package ir.co.ocs.envoriment.client;


import ir.co.ocs.envoriment.enums.State;
import ir.co.ocs.envoriment.networkchannel.AbstractNetworkChannel;
import ir.co.ocs.envoriment.networkchannel.NetworkChannel;
import ir.co.ocs.socketconfiguration.ClientSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationInterface;
import ir.co.ocs.statistics.Statistics;
import lombok.Getter;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

@Getter
public class Client extends AbstractNetworkChannel {
    private ConnectFuture future;
    private IoSession session;

    public Client(ClientSocketConfiguration clientSocketConfiguration, NioSocketConnector connector, SocketConfigurationInterface socketConfiguration, Statistics statistics) {
        super(clientSocketConfiguration, connector, socketConfiguration, statistics);
    }

    public Client(ClientSocketConfiguration clientSocketConfiguration, NioSocketConnector connector) {
        super(clientSocketConfiguration, connector);
    }

    @Override
    public void addProcessor() {

    }

    @Override
    public NetworkChannel start() throws RuntimeIoException {

        this.future = getConnector().connect(new InetSocketAddress(getClientConfig().getHost(), getClientConfig().getPort()));
        this.future.awaitUninterruptibly();
        session = future.getSession();
        this.setState(State.RUNNING);
        return this;
    }

    @Override
    public void stop() {
        if (session != null && session.isConnected()) {
            session.closeNow();
        }
        getConnector().dispose();
        this.setState(State.STOP);
    }


    @Override
    public NetworkChannel restart() throws RuntimeIoException {
        if (session != null && session.isConnected()) {
            this.setState(State.RESTARTING);
            session.closeNow();
            return start();
        } else {
            throw new IllegalArgumentException("Service with ID " + getIdentification() + " is not Running");
        }
    }


    public NioSocketConnector getConnector() {
        return (NioSocketConnector) ioService;
    }

    public ClientSocketConfiguration getClientConfig() {
        return (ClientSocketConfiguration) getConfiguration();
    }


}
