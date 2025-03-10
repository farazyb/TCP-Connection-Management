package ir.co.ocs.envoriment.client;

import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ClientFactory {
    public NioSocketConnector createClient() {
        return new NioSocketConnector();
    }
}
