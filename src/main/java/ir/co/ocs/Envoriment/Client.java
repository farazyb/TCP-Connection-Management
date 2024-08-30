package ir.co.ocs.envoriment;

import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Client {

    NioSocketConnector client;

    public Client() {
        client =new NioSocketConnector();
        client.setConnectTimeoutCheckInterval(10);
        client.setConnectTimeoutMillis(5000);

    }
}
