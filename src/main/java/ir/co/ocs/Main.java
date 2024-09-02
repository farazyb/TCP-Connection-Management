package ir.co.ocs;

import ir.co.ocs.envoriment.Server;
import ir.co.ocs.Handlers.TimeServerHandler;
import ir.co.ocs.codec.FixedLengthByteArrayFactory;
import ir.co.ocs.envoriment.ServerFactory;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import ir.co.ocs.socketconfiguration.enums.SocketMode;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;

import java.util.concurrent.TimeUnit;

public class Main {


    public static void main(String[] args) {
        ChannelInformation channelInformation = new ChannelInformation("Test", true);
        DefaultTcpSocketConfiguration defaultTcpSocketConfiguration = new DefaultTcpSocketConfiguration();
        defaultTcpSocketConfiguration.setReuseAddress(true);
       // defaultTcpSocketConfiguration.setSoLinger(1);
        defaultTcpSocketConfiguration.setPort(8080);

        if (defaultTcpSocketConfiguration == null) {
            defaultTcpSocketConfiguration = new DefaultTcpSocketConfiguration();
        }

        Server server = new Server(defaultTcpSocketConfiguration, channelInformation, new ServerFactory().createIoService());
//        server.addFilter("logging", new LoggingFilter());
//        server.addFilter("codec", new ProtocolCodecFilter(new FixedLengthByteArrayFactory()));
        server.setHandler(new TimeServerHandler());
        server.start();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
