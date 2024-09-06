package ir.co.ocs.envoriment.client;

import ir.co.ocs.socketconfiguration.ClientSocketConfiguration;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfiguration;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public final class TCPClient extends Client {
    public TCPClient(ClientSocketConfiguration clientSocketConfiguration, ClientFactory clientFactory, SocketConfiguration socketConfiguration) {
        super(clientSocketConfiguration, clientFactory.createServer(), socketConfiguration);
    }

    public TCPClient(ClientSocketConfiguration clientSocketConfiguration, ClientFactory clientFactory) {
        super(clientSocketConfiguration, clientFactory.createServer());
    }

}
