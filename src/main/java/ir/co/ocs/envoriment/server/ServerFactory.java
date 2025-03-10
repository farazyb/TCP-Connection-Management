package ir.co.ocs.envoriment.server;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class ServerFactory {
    public NioSocketAcceptor createServer() {
        return new NioSocketAcceptor();
    }
}
