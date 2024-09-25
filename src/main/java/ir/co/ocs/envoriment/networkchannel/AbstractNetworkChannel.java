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

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractNetworkChannel implements NetworkChannel, FilterManager, HandlerManager {

    protected Statistics statistics;
    protected IoService ioService;
    protected CountDownLatch latch;
    protected Thread serverThread;
    @Setter
    protected volatile State state = State.NONE;
    private BaseTcpSocketConfiguration baseTcpSocketConfiguration;
    private final SocketConfigurationInterface socketConfiguration;


    protected AbstractNetworkChannel(BaseTcpSocketConfiguration defaultTcpSocketConfiguration, IoService ioService, SocketConfigurationInterface socketConfiguration, Statistics statistics) {
        this.socketConfiguration = socketConfiguration;
        this.ioService = ioService;
        this.statistics = statistics;
        setDefaultTcpSocketConfiguration(defaultTcpSocketConfiguration);
        setDefaultFilter(ioService);
        applyConfig(this.ioService);


    }

    protected AbstractNetworkChannel(BaseTcpSocketConfiguration defaultTcpSocketConfiguration, IoService ioService) {
        this.socketConfiguration = new SocketConfigurationHandler();
        this.ioService = ioService;
        this.statistics = new DefaultStatistics();
        setDefaultTcpSocketConfiguration(defaultTcpSocketConfiguration);
        setDefaultFilter(ioService);
        applyConfig(ioService);

    }

    private void setDefaultTcpSocketConfiguration(BaseTcpSocketConfiguration defaultTcpSocketConfiguration) {
        if (Objects.isNull(defaultTcpSocketConfiguration)) {
            throw new NullPointerException("defaultTcpSocketConfiguration must not be Null");
        }
        this.baseTcpSocketConfiguration = defaultTcpSocketConfiguration;
    }

    private void applyConfig(IoService ioService) {
        if (ioService instanceof IoAcceptor ioAcceptor) {
            this.socketConfiguration.applyConfig(ioAcceptor, (ServerSocketConfiguration) baseTcpSocketConfiguration);
        } else if (ioService instanceof IoConnector ioConnector) {
            this.socketConfiguration.applyConfig(ioConnector, (ClientSocketConfiguration) baseTcpSocketConfiguration);
        }
    }

    @Override
    public BaseTcpSocketConfiguration getConfiguration() {
        return this.baseTcpSocketConfiguration;
    }

    @Override
    public void setDefaultFilter(IoService ioService) {
        DefaultIoFilterChainBuilder filterChainBuilder = ioService.getFilterChain();
        filterChainBuilder.addFirst("Attribute", new AddChannelAttributeFiler(getConfiguration().getChannelAttribute()));
        filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new FixedLengthByteArrayFactory()));
        filterChainBuilder.addLast("LoggingFilter", new LoggingFilter(baseTcpSocketConfiguration.getChannelIdentificationName()));
        filterChainBuilder.addLast("sessionStats", getStatisticFilter());
        addSSL(filterChainBuilder);

    }

    private void addSSL(DefaultIoFilterChainBuilder filterChainBuilder) {
        SSLManger sslManger = getConfiguration().getSslManger();
        if (sslManger != null) {
            SSLContext sslContext = null;
            try {
                sslContext = sslManger.createSSLContext();
                if (sslContext != null) {
                    filterChainBuilder.addLast("SSL", new SslFilter(getConfiguration().getSslManger().createSSLContext()));
                } else {
                    throw new IllegalArgumentException("SSLContext is not Valid, For Secure tunnel please provide SSLContext");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


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

    public String getIdentification() {
        String identificationName = getConfiguration().getChannelIdentificationName();
        if (identificationName == null || identificationName.isEmpty()) {
            throw new RuntimeException("Identification name must set");
        }
        return identificationName;
    }

    public boolean isActive() {
        return this.state != State.STOP;
    }


}
