package ir.co.ocs.Envoriment;

import ir.co.ocs.ChannelInformation;
import ir.co.ocs.Handlers.NetWorkChannelHandler;
import org.apache.mina.core.IoUtil;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class Server implements NetworkChannel {
    private IoAcceptor server;
    private int port;
    private CountDownLatch latch;
    private Thread serverThread;
    private ChannelInformation channelInformation;

    public Server(ChannelInformation channelInformation) {
        this.channelInformation = channelInformation;
    }

    public void create() {
        server = new NioSocketAcceptor();
        DefaultSocketSessionConfig defaultSocketSessionConfig = new DefaultSocketSessionConfig();
        defaultSocketSessionConfig.setKeepAlive(true);
        server.getSessionConfig().setAll(defaultSocketSessionConfig);

    }

    @Override
    public void addProcessor() {

    }

    public void addFilter(String name, IoFilter filterChain) {

        server.getFilterChain().addLast(name, filterChain);
    }

    public void setHandler(NetWorkChannelHandler handler) {
        handler.setChannelInformation(channelInformation);
        server.setHandler(handler);
    }

    private void bind(int port) throws IOException {
        server.bind(new InetSocketAddress(port));
    }

    @Override
    public NetworkChannel restart() {
        stop();
        start(this.port);
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
    public NetworkChannel start(int port) {
        this.port = port;
        latch = new CountDownLatch(1); // Initialize the latch

        serverThread = new Thread(() -> {
            try {
                bind(port);
                latch.await(); // Wait indefinitely until the latch is counted down
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
            } finally {
                server.unbind(); // Unbind the server
                server.dispose(); // Clean up resources when the server stops
            }
        });

        serverThread.start(); // Start the server thread
        return this;
    }
}
