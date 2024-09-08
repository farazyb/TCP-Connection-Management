package ir.co.ocs.envoriment.networkchannel;

import ir.co.ocs.Handlers.HandlerManager;
import ir.co.ocs.filters.FilterManager;
import ir.co.ocs.socketconfiguration.*;
import ir.co.ocs.statistics.DefaultStatistics;
import ir.co.ocs.Handlers.NetworkChannelHandler;
import ir.co.ocs.statistics.Statistics;
import ir.co.ocs.codec.FixedLengthByteArrayFactory;
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

public abstract class AbstractNetworkChannel implements NetworkChannel, FilterManager, HandlerManager {

    protected Statistics statistics;
    protected IoService ioService;
    protected CountDownLatch latch;
    protected Thread serverThread;
    protected volatile boolean stop = false;
    private BaseTcpSocketConfiguration baseTcpSocketConfiguration;
    private final SocketConfigurationInterface socketConfiguration;


    protected AbstractNetworkChannel(BaseTcpSocketConfiguration defaultTcpSocketConfiguration
            , IoService ioService
            , SocketConfigurationInterface socketConfiguration
            , Statistics statistics) {
        this.socketConfiguration = socketConfiguration;
        this.ioService = ioService;
        this.statistics = statistics;
        setDefaultTcpSocketConfiguration(defaultTcpSocketConfiguration);
        setDefaultFilter(ioService);
        applyConfig(this.ioService);


    }

    protected AbstractNetworkChannel(BaseTcpSocketConfiguration defaultTcpSocketConfiguration
            , IoService ioService) {
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
        filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new FixedLengthByteArrayFactory()));
        filterChainBuilder.addLast("LoggingFilter", new LoggingFilter(baseTcpSocketConfiguration.getChannelIdentificationName()));
        filterChainBuilder.addLast("sessionStats", getStatisticFilter());

    }

    @Override
    public void setHandler(NetworkChannelHandler handler) {
        handler.setChannelAttribute(this.baseTcpSocketConfiguration.getChannelAttribute());
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

    public boolean shouldStop() {
        return stop;
    }
}
