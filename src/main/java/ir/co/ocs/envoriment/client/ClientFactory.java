package ir.co.ocs.envoriment.client;

import ir.co.ocs.envoriment.ioservicefactory.IoServiceFactory;
import org.apache.mina.core.service.IoService;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ClientFactory implements IoServiceFactory {
    @Override
    public NioSocketConnector createServer() {
        return new NioSocketConnector();
    }
}
