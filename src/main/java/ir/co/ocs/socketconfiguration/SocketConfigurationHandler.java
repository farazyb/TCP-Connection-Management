package ir.co.ocs.socketconfiguration;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSessionConfig;

public class SocketConfigurationHandler implements SocketConfigurationInterface {


    public void applyConfig(IoAcceptor acceptor, DefaultTcpSocketConfiguration config) {
        IoSessionConfig sessionConfig = acceptor.getSessionConfig();
        sessionConfig.setAll(config);
    }

    public void applyConfig(IoConnector connector, DefaultTcpSocketConfiguration config) {
        IoSessionConfig sessionConfig = connector.getSessionConfig();
        sessionConfig.setAll(config);
    }
}
