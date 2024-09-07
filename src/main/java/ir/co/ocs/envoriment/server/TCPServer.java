package ir.co.ocs.envoriment.server;

import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationInterface;
import ir.co.ocs.statistics.Statistics;

public final class TCPServer extends Server {
    public TCPServer(ServerSocketConfiguration serverSocketConfiguration, ServerFactory serverFactory, SocketConfigurationInterface socketConfiguration, Statistics statistics) {
        super(serverSocketConfiguration, serverFactory.createServer(), socketConfiguration,statistics);
    }

    public TCPServer(ServerSocketConfiguration serverSocketConfiguration, ServerFactory serverFactory) {
        super(serverSocketConfiguration, serverFactory.createServer());
    }
    
}
