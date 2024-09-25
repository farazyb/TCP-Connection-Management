package ir.co.ocs.socketconfiguration;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;

public interface SocketConfigurationInterface {

    void applyConfig(IoAcceptor acceptor, ServerSocketConfiguration config);

    void applyConfig(IoConnector acceptor, ClientSocketConfiguration config);
}
