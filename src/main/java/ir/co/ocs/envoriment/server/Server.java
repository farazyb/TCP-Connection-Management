package ir.co.ocs.envoriment.server;

import ir.co.ocs.envoriment.enums.State;
import ir.co.ocs.envoriment.networkchannel.AbstractNetworkChannel;
import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationInterface;
import ir.co.ocs.statistics.Statistics;
import lombok.extern.log4j.Log4j2;
import org.apache.mina.core.service.IoService;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Log4j2
public abstract class Server extends AbstractNetworkChannel {
    private CountDownLatch latch;
    private Thread serverThread;

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
        getAcceptor().bind(new InetSocketAddress(getServerConfig().getPort()));
    }

    @Override
    public Server restart() {
        setState(State.RESTARTING);
        if (latch != null) {
            latch.countDown();
        }
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            getAcceptor().unbind();
        }
        return this;
    }

    @Override
    public void stop() {
        setState(State.STOP);
        if (latch != null) {
            latch.countDown();
        }
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            getAcceptor().unbind();
            getAcceptor().dispose();
        }
    }

    @Override
    public Server start() {
        setState(State.RUNNING);
        latch = new CountDownLatch(1);
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        serverThread = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Started");
                bind();
                future.complete(true);
                latch.await();
            } catch (IOException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            } finally {
                if (latch != null) {
                    latch.countDown();
                }
            }
            log.warn(Thread.currentThread().getName() + " Stopped");
        });

        serverThread.start();
        future.thenAccept(isBound -> {
            if (isBound) {
                log.info("Server " + this.getIdentification() + " Starts Listening on port:" + this.getServerConfig().getPort());
            }
        }).exceptionally(e -> {
            log.error("Server failed to bind: " + e.getMessage());
            throw new RuntimeException(e);
        });
        return this;
    }

    public CompletableFuture<Boolean> startService() {
        setState(State.RUNNING);
        latch = new CountDownLatch(1);
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        serverThread = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Started");
                bind();
                future.complete(true);
                latch.await();
            } catch (IOException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            } finally {
                System.out.println(Thread.currentThread().getName() + " Stopped");
                if (latch != null) {
                    latch.countDown();
                }
            }
        });

        serverThread.start();
        return future;
    }

    public ServerSocketConfiguration getServerConfig() {
        return (ServerSocketConfiguration) getBaseTcpSocketConfiguration();
    }
}
