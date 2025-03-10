package ir.co.ocs.envoriment.client;

import ir.co.ocs.socketconfiguration.ClientSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationInterface;
import ir.co.ocs.statistics.Statistics;
import org.apache.mina.core.service.IoService;

public final class TCPClient extends Client {
    public TCPClient(ClientSocketConfiguration clientSocketConfiguration, ClientFactory clientFactory, SocketConfigurationInterface socketConfiguration, Statistics statistics) {
        super(clientSocketConfiguration, clientFactory.createClient(), socketConfiguration, statistics);
    }

    public TCPClient(ClientSocketConfiguration clientSocketConfiguration, ClientFactory clientFactory) {
        super(clientSocketConfiguration, clientFactory.createClient());
    }

    @Override
    protected void doInitialize() throws Exception {
        // TCP client initialization logic
    }

    @Override
    protected void doStart() throws Exception {
        // Connection is handled by the parent class
    }

    @Override
    protected void doStop() throws Exception {
        if (session != null && session.isConnected()) {
            session.closeNow();
        }
        getConnector().dispose();
    }

    @Override
    public void setDefaultFilter(IoService ioService) {
        // TCP client filter setup
    }

    @Override
    public boolean isRunning() {
        return session != null && session.isConnected();
    }
}
