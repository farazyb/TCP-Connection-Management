package ir.co.ocs.envoriment;

import ir.co.ocs.ChannelInformation;
import ir.co.ocs.Handlers.NetworkChannelHandler;
import ir.co.ocs.filters.SessionStatisticsFilter;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationHandler;
import lombok.extern.log4j.Log4j;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoService;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

@Log4j
public class Server extends AbstractNetworkChannel {
    private NioSocketAcceptor acceptor;
    private ChannelInformation channelInformation;

    public Server(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration, ChannelInformation channelInformation, IoService acceptor, SocketConfiguration socketConfiguration) {
        super(defaultTcpSocketConfiguration, channelInformation, acceptor, socketConfiguration);
    }

    public Server(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration, ChannelInformation channelInformation, IoService acceptor) {
        super(defaultTcpSocketConfiguration, channelInformation, acceptor);
    }


    @Override
    public void addProcessor() {

    }

    public void addFilter(String name, IoFilter filterChain) {

        acceptor.getFilterChain().addLast(name, filterChain);
    }

    public void setHandler(NetworkChannelHandler handler) {
        handler.setChannelInformation(channelInformation);
        handler.setChannelAttribute(getDefaultTcpSocketConfiguration().getChannelAttribute());
        acceptor.setHandler(handler);
    }

    private void bind() throws IOException {
        acceptor.bind(new InetSocketAddress(getDefaultTcpSocketConfiguration().getPort()));
    }

    @Override
    public NetworkChannel restart() {
        stop();
        start();
        return this;
    }

    @Override
    public NetworkChannel stop() {
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
    public NetworkChannel start() {
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
                acceptor.unbind(); // Unbind the server
                acceptor.dispose(); // Clean up resources when the server stops
            }
        });

        serverThread.start(); // Start the server thread
        return this;
    }
}
