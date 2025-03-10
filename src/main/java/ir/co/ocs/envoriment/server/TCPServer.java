package ir.co.ocs.envoriment.server;

import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationInterface;
import ir.co.ocs.statistics.Statistics;
import org.apache.mina.core.service.IoService;

public final class TCPServer extends Server {
    public TCPServer(ServerSocketConfiguration serverSocketConfiguration, ServerFactory serverFactory, SocketConfigurationInterface socketConfiguration, Statistics statistics) {
        super(serverSocketConfiguration, serverFactory.createServer(), socketConfiguration, statistics);
    }

    public TCPServer(ServerSocketConfiguration serverSocketConfiguration, ServerFactory serverFactory) {
        super(serverSocketConfiguration, serverFactory.createServer());
    }

    @Override
    protected void doInitialize() throws Exception {
        // TCP server initialization logic
    }

    @Override
    protected void doStart() throws Exception {
        bind();
    }

    @Override
    protected void doStop() throws Exception {
        getAcceptor().unbind();
        getAcceptor().dispose();
    }

    @Override
    public void setDefaultFilter(IoService ioService) {
        // TCP server filter setup
    }

    @Override
    public boolean isRunning() {
        return getAcceptor().isActive();
    }
}
