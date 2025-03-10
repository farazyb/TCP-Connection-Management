package ir.co.ocs.envoriment.networkchannel;

import ir.co.ocs.Handlers.HandlerManager;
import ir.co.ocs.SessionManager;
import ir.co.ocs.envoriment.enums.State;
import ir.co.ocs.filters.AddChannelAttributeFiler;
import ir.co.ocs.filters.FilterManager;
import ir.co.ocs.socketconfiguration.*;
import ir.co.ocs.statistics.DefaultStatistics;
import ir.co.ocs.Handlers.NetworkChannelHandler;
import ir.co.ocs.statistics.Statistics;
import ir.co.ocs.codec.FixedLengthByteArrayFactory;
import lombok.Setter;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoService;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract base class for network channel implementations.
 * This class provides common functionality and state management for network channels,
 * including lifecycle management and configuration handling.
 *
 * @author OCS Team
 * @version 1.0
 */
public abstract class AbstractNetworkChannel implements NetworkChannel, FilterManager, HandlerManager, ServiceLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(AbstractNetworkChannel.class);

    protected Statistics statistics;
    protected IoService ioService;
    protected CountDownLatch latch;
    protected Thread serverThread;
    @Setter
    protected volatile State state = State.NONE;
    private BaseTcpSocketConfiguration baseTcpSocketConfiguration;
    protected final String identification;
    protected final SocketConfigurationInterface configuration;
    protected final AtomicBoolean isRunning;
    protected final AtomicBoolean isInitialized;

    /**
     * Constructs a new AbstractNetworkChannel with the specified configuration and service.
     *
     * @param configuration The socket configuration
     * @param ioService The IO service (acceptor or connector)
     * @param socketConfig The socket configuration interface
     * @param statistics The statistics handler
     */
    protected AbstractNetworkChannel(BaseTcpSocketConfiguration configuration, IoService ioService, 
            SocketConfigurationInterface socketConfig, Statistics statistics) {
        this.identification = configuration.getChannelIdentificationName();
        this.configuration = socketConfig;
        this.ioService = ioService;
        this.statistics = statistics;
        this.isRunning = new AtomicBoolean(false);
        this.isInitialized = new AtomicBoolean(false);
        setDefaultTcpSocketConfiguration(configuration);
        setDefaultFilter(ioService);
        applyConfig(ioService);
    }

    /**
     * Constructs a new AbstractNetworkChannel with the specified configuration and service.
     *
     * @param configuration The socket configuration
     * @param ioService The IO service (acceptor or connector)
     */
    protected AbstractNetworkChannel(BaseTcpSocketConfiguration configuration, IoService ioService) {
        this(configuration, ioService, new SocketConfigurationHandler(), new DefaultStatistics());
    }

    /**
     * Initializes the network channel.
     * This method should be called before starting the channel.
     *
     * @throws IllegalStateException if the channel is already initialized
     */
    @Override
    public void initialize() {
        if (isInitialized.get()) {
            throw new IllegalStateException("Channel is already initialized");
        }
        try {
            doInitialize();
            isInitialized.set(true);
            logger.info("Channel {} initialized successfully", identification);
        } catch (Exception e) {
            logger.error("Failed to initialize channel {}", identification, e);
            throw new RuntimeException("Channel initialization failed", e);
        }
    }

    /**
     * Starts the network channel.
     * This method should be called after initialization.
     *
     * @return The network channel instance for method chaining
     * @throws IllegalStateException if the channel is not initialized or is already running
     */
    @Override
    public NetworkChannel start() {
        if (!isInitialized.get()) {
            throw new IllegalStateException("Channel must be initialized before starting");
        }
        if (isRunning.get()) {
            throw new IllegalStateException("Channel is already running");
        }
        try {
            doStart();
            isRunning.set(true);
            logger.info("Channel {} started successfully", identification);
            return this;
        } catch (Exception e) {
            logger.error("Failed to start channel {}", identification, e);
            throw new RuntimeException("Channel start failed", e);
        }
    }

    @Override
    public CompletableFuture<Boolean> startService() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        try {
            start();
            future.complete(true);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    /**
     * Stops the network channel.
     * This method should be called when the channel is no longer needed.
     *
     * @throws IllegalStateException if the channel is not running
     */
    @Override
    public void stop() {
        if (!isRunning.get()) {
            throw new IllegalStateException("Channel is not running");
        }
        try {
            doStop();
            isRunning.set(false);
            logger.info("Channel {} stopped successfully", identification);
        } catch (Exception e) {
            logger.error("Failed to stop channel {}", identification, e);
            throw new RuntimeException("Channel stop failed", e);
        }
    }

    /**
     * Restarts the network channel.
     * This method performs a complete stop and start sequence.
     *
     * @return The network channel instance for method chaining
     */
    @Override
    public NetworkChannel restart() {
        stop();
        return start();
    }

    /**
     * Checks if the network channel is currently active.
     *
     * @return true if the channel is active, false otherwise
     */
    @Override
    public boolean isActive() {
        return isRunning.get();
    }

    /**
     * Gets the unique identification of the network channel.
     *
     * @return The channel's identification string
     */
    @Override
    public String getIdentification() {
        return identification;
    }

    /**
     * Gets the configuration for this network channel.
     *
     * @return The socket configuration interface
     */
    @Override
    public SocketConfigurationInterface getConfiguration() {
        return configuration;
    }

    /**
     * Performs the actual initialization of the channel.
     * This method should be implemented by subclasses to provide specific initialization logic.
     *
     * @throws Exception if initialization fails
     */
    protected abstract void doInitialize() throws Exception;

    /**
     * Performs the actual start of the channel.
     * This method should be implemented by subclasses to provide specific start logic.
     *
     * @throws Exception if start fails
     */
    protected abstract void doStart() throws Exception;

    /**
     * Performs the actual stop of the channel.
     * This method should be implemented by subclasses to provide specific stop logic.
     *
     * @throws Exception if stop fails
     */
    protected abstract void doStop() throws Exception;

    private void setDefaultTcpSocketConfiguration(BaseTcpSocketConfiguration defaultTcpSocketConfiguration) {
        if (Objects.isNull(defaultTcpSocketConfiguration)) {
            throw new NullPointerException("defaultTcpSocketConfiguration must not be Null");
        }
        this.baseTcpSocketConfiguration = defaultTcpSocketConfiguration;
    }

    private void applyConfig(IoService ioService) {
        if (ioService instanceof IoAcceptor ioAcceptor) {
            this.configuration.applyConfig(ioAcceptor, (ServerSocketConfiguration) baseTcpSocketConfiguration);
        } else if (ioService instanceof IoConnector ioConnector) {
            this.configuration.applyConfig(ioConnector, (ClientSocketConfiguration) baseTcpSocketConfiguration);
        }
    }

    @Override
    public void setHandler(NetworkChannelHandler handler) {
        this.ioService.setHandler(handler);
    }

    @Override
    public void addFilter(String name, IoFilterAdapter filterChain) {
        ioService.getFilterChain().addLast(name, filterChain);
    }

    public Map<String, Object> getStatistics() {
        return statistics.getStatistics();
    }

    public IoFilterAdapter getStatisticFilter() {
        return this.statistics.getStatisticsFilter();
    }

    /**
     * Adds SSL support to the channel if configured.
     *
     * @param filterChainBuilder The filter chain builder to add SSL filters to
     */
    protected void addSSL(DefaultIoFilterChainBuilder filterChainBuilder) {
        try {
            SSLManger sslManager = SSLManger.getInstance();
            SSLContext sslContext = sslManager.getSSLContext();
            SslFilter sslFilter = new SslFilter(sslContext);
            filterChainBuilder.addLast("ssl", sslFilter);
        } catch (Exception e) {
            logger.error("Failed to add SSL filter to channel {}", identification, e);
            throw new RuntimeException("SSL filter addition failed", e);
        }
    }

    protected BaseTcpSocketConfiguration getBaseTcpSocketConfiguration() {
        return baseTcpSocketConfiguration;
    }
}
