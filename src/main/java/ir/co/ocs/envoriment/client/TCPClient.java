package ir.co.ocs.envoriment.client;

import ir.co.ocs.socketconfiguration.ClientSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationInterface;
import ir.co.ocs.statistics.Statistics;

public final class TCPClient extends Client {
   public  TCPClient(ClientSocketConfiguration clientSocketConfiguration, ClientFactory clientFactory, SocketConfigurationInterface socketConfiguration, Statistics statistics) {
        super(clientSocketConfiguration, clientFactory.createServer(), socketConfiguration, statistics);
    }

    public TCPClient(ClientSocketConfiguration clientSocketConfiguration, ClientFactory clientFactory) {
        super(clientSocketConfiguration, clientFactory.createServer());
    }

}
