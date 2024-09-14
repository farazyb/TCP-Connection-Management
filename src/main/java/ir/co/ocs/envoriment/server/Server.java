package ir.co.ocs.envoriment.server;

import ir.co.ocs.envoriment.enums.State;
import ir.co.ocs.envoriment.networkchannel.AbstractNetworkChannel;
import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationInterface;
import ir.co.ocs.statistics.Statistics;
import lombok.extern.log4j.Log4j;
import org.apache.mina.core.service.IoService;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Log4j
public abstract class Server extends AbstractNetworkChannel {


    public Server(ServerSocketConfiguration serverSocketConfiguration, NioSocketAcceptor acceptor, SocketConfigurationInterface socketConfiguration, Statistics statistics) {
        super(serverSocketConfiguration, acceptor, socketConfiguration, statistics);

    }

    public Server(ServerSocketConfiguration serverSocketConfiguration, IoService acceptor) {
        super(serverSocketConfiguration, acceptor);

    }

    public NioSocketAcceptor getAcceptor() {
        return (NioSocketAcceptor) ioService;
    }


    public void bind() throws IOException {
        getAcceptor().bind(new InetSocketAddress(getConfiguration().getPort()));
    }

    @Override
    public Server restart() {
        setState(State.RESTARTING);
        if (latch != null) {
            latch.countDown(); // Signal the server to stop
        }
        try {
            serverThread.join(); // Wait for the server thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        } finally {
            getAcceptor().unbind(); // Unbind the server
        }

        return this;
    }

    @Override
    public void stop() {
        setState(State.STOP);
        if (latch != null) {
            latch.countDown(); // Signal the server to stop
        }
        try {
            serverThread.join(); // Wait for the server thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        } finally {
            getAcceptor().unbind(); // Unbind the server
            getAcceptor().dispose();
        }

    }

    @Override
    public Server start() {

        setState(State.RUNNING);
        latch = new CountDownLatch(1); // Initialize the latch
        serverThread = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Started");
                bind();
                latch.await(); // Wait indefinitely until the latch is counted down
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
            } finally {
                System.out.println(Thread.currentThread().getName() + " Stopped");
            }
        });

        serverThread.start(); // Start the server thread
        return this;
    }

    public CompletableFuture<Boolean> startService(){
        setState(State.RUNNING);
        latch = new CountDownLatch(1); // Initialize the latch
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        serverThread = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Started");
                bind();
                future.complete(true); // Complete the future successfully
                latch.await(); // Wait indefinitely until the latch is counted down
            } catch (IOException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e); // Complete with exception
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                future.completeExceptionally(e);
            } finally {
                System.out.println(Thread.currentThread().getName() + " Stopped");
                if (latch != null) {
                    latch.countDown(); // Signal the server to stop
                }
            }
        });

        serverThread.start(); // Start the server thread
        return future;
    }


    @Override
    public void addProcessor() {

    }

    public ServerSocketConfiguration getServerConfig() {
        return (ServerSocketConfiguration) getConfiguration();
    }

}
