package ir.co.ocs.envoriment.server;

import ir.co.ocs.envoriment.ioservicefactory.IoServiceFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


public class ServerFactory implements IoServiceFactory {

    @Override
    public NioSocketAcceptor createServer() {
        return new NioSocketAcceptor();
    }
}
