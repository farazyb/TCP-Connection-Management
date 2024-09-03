package ir.co.ocs.envoriment;

import ir.co.ocs.ChannelInformation;
import ir.co.ocs.codec.FixedLengthByteArrayFactory;
import ir.co.ocs.filters.SessionStatisticsFilter;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfiguration;
import ir.co.ocs.socketconfiguration.SocketConfigurationHandler;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoService;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractNetworkChannel implements NetworkChannel, ServerLifecycle {

    protected IoService ioService;
    protected CountDownLatch latch;
    protected Thread serverThread;

    private DefaultTcpSocketConfiguration defaultTcpSocketConfiguration;
    private final SocketConfiguration socketConfiguration;


    public AbstractNetworkChannel(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration
            , ChannelInformation channelInformation
            , IoService acceptor
            , SocketConfiguration socketConfiguration) {
        this.socketConfiguration = socketConfiguration;
        this.ioService = (NioSocketAcceptor) acceptor;
        setDefaultHandler(acceptor);
    }

    public AbstractNetworkChannel(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration
            , ChannelInformation channelInformation
            , IoService acceptor) {
        this.socketConfiguration = new SocketConfigurationHandler();
        this.ioService = (NioSocketAcceptor) acceptor;
        setDefaultHandler(acceptor);
        applyConfig(acceptor);
    }

    private void setDefaultTcpSocketConfiguration(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration) {
        if (Objects.isNull(defaultTcpSocketConfiguration)) {
            throw new NullPointerException("defaultTcpSocketConfiguration must not be Null");
        }
        this.defaultTcpSocketConfiguration = defaultTcpSocketConfiguration;
    }

    public void applyConfig(IoService ioService) {
        if (ioService instanceof IoAcceptor ioAcceptor) {
            this.socketConfiguration.applyConfig(ioAcceptor, defaultTcpSocketConfiguration);
        } else if (ioService instanceof IoConnector ioConnector) {
            this.socketConfiguration.applyConfig(ioConnector, defaultTcpSocketConfiguration);
        }
    }

    protected DefaultTcpSocketConfiguration getDefaultTcpSocketConfiguration() {
        return defaultTcpSocketConfiguration;
    }

    @Override
    public void setDefaultHandler(IoService ioService) {
        DefaultIoFilterChainBuilder filterChainBuilder = ioService.getFilterChain();
        filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new FixedLengthByteArrayFactory()));
        filterChainBuilder.addLast("LoggingFilter", new LoggingFilter(defaultTcpSocketConfiguration.getChannelIdentificationName()));
        filterChainBuilder.addLast("statistic", new SessionStatisticsFilter());

    }
}
