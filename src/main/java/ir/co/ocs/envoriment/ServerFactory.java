package ir.co.ocs.envoriment;

import org.apache.mina.core.service.IoService;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


public class ServerFactory implements IoServiceFactory {

    @Override
    public IoService createIoService() {
        return new NioSocketAcceptor();
    }
}
