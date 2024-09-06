package ir.co.ocs.envoriment.networkchannel;

import ir.co.ocs.statistics.DefaultStatistics;
import ir.co.ocs.Handlers.NetworkChannelHandler;
import ir.co.ocs.statistics.Statistics;
import ir.co.ocs.codec.FixedLengthByteArrayFactory;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationHandler;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoService;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractNetworkChannel implements NetworkChannel, ServiceLifecycle {

    protected Statistics statistics;
    protected IoService ioService;
    protected CountDownLatch latch;
    protected Thread serverThread;

    private DefaultTcpSocketConfiguration defaultTcpSocketConfiguration;
    private final SocketConfiguration socketConfiguration;


    protected AbstractNetworkChannel(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration
            , IoService ioService
            , SocketConfiguration socketConfiguration
            , Statistics statistics) {
        this.socketConfiguration = socketConfiguration;
        this.ioService = ioService;
        setDefaultTcpSocketConfiguration(defaultTcpSocketConfiguration);
        setDefaultFilter(ioService);
        applyConfig(this.ioService);
        this.statistics = statistics;

    }

    protected AbstractNetworkChannel(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration
            , IoService ioService) {
        this.socketConfiguration = new SocketConfigurationHandler();
        this.ioService = ioService;
        setDefaultTcpSocketConfiguration(defaultTcpSocketConfiguration);
        setDefaultFilter(ioService);
        applyConfig(ioService);
        this.statistics = new DefaultStatistics();
    }

    private void setDefaultTcpSocketConfiguration(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration) {
        if (Objects.isNull(defaultTcpSocketConfiguration)) {
            throw new NullPointerException("defaultTcpSocketConfiguration must not be Null");
        }
        this.defaultTcpSocketConfiguration = defaultTcpSocketConfiguration;
    }

    private void applyConfig(IoService ioService) {
        if (ioService instanceof IoAcceptor ioAcceptor) {
            this.socketConfiguration.applyConfig(ioAcceptor, defaultTcpSocketConfiguration);
        } else if (ioService instanceof IoConnector ioConnector) {
            this.socketConfiguration.applyConfig(ioConnector, defaultTcpSocketConfiguration);
        }
    }

    @Override
    public DefaultTcpSocketConfiguration getConfiguration() {
        return this.defaultTcpSocketConfiguration;
    }

    @Override
    public void setDefaultFilter(IoService ioService) {
        DefaultIoFilterChainBuilder filterChainBuilder = ioService.getFilterChain();
        filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new FixedLengthByteArrayFactory()));
        filterChainBuilder.addLast("LoggingFilter", new LoggingFilter(defaultTcpSocketConfiguration.getChannelIdentificationName()));
        filterChainBuilder.addLast("sessionStats", getStatisticFilter());

    }

    public void setHandler(NetworkChannelHandler handler) {
        handler.setChannelAttribute(this.defaultTcpSocketConfiguration.getChannelAttribute());
        this.ioService.setHandler(handler);
    }

    public void addFilter(String name, IoFilter filterChain) {

        ioService.getFilterChain().addLast(name, filterChain);
    }

    public Map<String, Object> getStatistics() {
        return statistics.getStatistics();
    }

    public IoFilterAdapter getStatisticFilter() {
        return statistics.getStatisticsFilter();
    }

    public String getIdentification() {
        String identificationName = getConfiguration().getChannelIdentificationName();
        if (identificationName == null || identificationName.isEmpty()) {
            throw new RuntimeException("Identification name must set");
        }
        return identificationName;
    }
}
