package ir.co.ocs.envoriment.server;

import ir.co.ocs.envoriment.server.Server;
import ir.co.ocs.envoriment.server.ServerFactory;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfiguration;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public final class TCPServer extends Server {
    public TCPServer(ServerSocketConfiguration serverSocketConfiguration, ServerFactory serverFactory, SocketConfiguration socketConfiguration) {
        super(serverSocketConfiguration, serverFactory.createServer(), socketConfiguration);
    }

    public TCPServer(ServerSocketConfiguration serverSocketConfiguration, ServerFactory serverFactory) {
        super(serverSocketConfiguration, serverFactory.createServer());
    }
    
}
