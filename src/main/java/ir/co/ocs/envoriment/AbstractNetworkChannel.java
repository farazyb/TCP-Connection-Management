package ir.co.ocs.envoriment;

import ir.co.ocs.codec.FixedLengthByteArrayFactory;
import ir.co.ocs.filters.SessionStatisticsFilter;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.service.IoService;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;

import java.util.Objects;

public abstract class AbstractNetworkChannel implements NetworkChannel {

    private DefaultTcpSocketConfiguration defaultTcpSocketConfiguration;

    public AbstractNetworkChannel(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration) {
        setDefaultTcpSocketConfiguration(defaultTcpSocketConfiguration);
    }

    protected void setDefaultTcpSocketConfiguration(DefaultTcpSocketConfiguration defaultTcpSocketConfiguration) {
        if (Objects.isNull(defaultTcpSocketConfiguration)) {
            throw new NullPointerException("defaultTcpSocketConfiguration must not be Null");
        }
        this.defaultTcpSocketConfiguration = defaultTcpSocketConfiguration;
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
