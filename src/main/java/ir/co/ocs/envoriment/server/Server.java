package ir.co.ocs.envoriment.server;

import ir.co.ocs.envoriment.networkchannel.AbstractNetworkChannel;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfiguration;
import ir.co.ocs.statistics.Statistics;
import lombok.extern.log4j.Log4j;
import org.apache.mina.core.service.IoService;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

@Log4j
public abstract class Server extends AbstractNetworkChannel {


    public Server(ServerSocketConfiguration serverSocketConfiguration, NioSocketAcceptor acceptor, SocketConfiguration socketConfiguration, Statistics statistics) {
        super(serverSocketConfiguration, acceptor, socketConfiguration, statistics);

    }

    public Server(ServerSocketConfiguration serverSocketConfiguration, IoService acceptor) {
        super(serverSocketConfiguration, acceptor);

    }

    protected NioSocketAcceptor nioSocketAcceptor() {
        return (NioSocketAcceptor) ioService;
    }


    public void bind() throws IOException {
        nioSocketAcceptor().bind(new InetSocketAddress(getConfiguration().getPort()));
    }

    @Override
    public Server restart() {
        stop();
        start();
        return this;
    }

    @Override
    public Server stop() {
        if (latch != null) {
            latch.countDown(); // Signal the server to stop
        }
        try {
            serverThread.join(); // Wait for the server thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
        return this;
    }

    @Override
    public Server start() {
        latch = new CountDownLatch(1); // Initialize the latch

        serverThread = new Thread(() -> {
            try {
                bind();
                latch.await(); // Wait indefinitely until the latch is counted down
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
            } finally {
                nioSocketAcceptor().unbind(); // Unbind the server
                nioSocketAcceptor().dispose(); // Clean up resources when the server stops
            }
        });

        serverThread.start(); // Start the server thread
        return this;
    }

    @Override
    public void addProcessor() {

    }

}
