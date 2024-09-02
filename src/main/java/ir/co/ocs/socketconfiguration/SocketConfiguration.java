package ir.co.ocs.socketconfiguration;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;

public interface SocketConfiguration {

    void applyConfig(IoAcceptor acceptor, DefaultTcpSocketConfiguration config);

    void applyConfig(IoConnector acceptor, DefaultTcpSocketConfiguration config);
}
