package ir.co.ocs.socketconfiguration;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;

public class SocketConfigurationHandler {
    public void applyConfig(IoService ioService, DefaultTcpSocketConfiguration config) {
        if (ioService instanceof IoAcceptor) {
            applyConfig((IoAcceptor) ioService, config);
        } else if (ioService instanceof IoConnector) {
            applyConfig((IoConnector) ioService, config);
        }
    }

    private void applyConfig(IoAcceptor acceptor, DefaultTcpSocketConfiguration config) {
        IoSessionConfig sessionConfig = acceptor.getSessionConfig();
        sessionConfig.setAll(config);
    }

    private void applyConfig(IoConnector connector, DefaultTcpSocketConfiguration config) {
        IoSessionConfig sessionConfig = connector.getSessionConfig();
        sessionConfig.setAll(config);
    }
}
