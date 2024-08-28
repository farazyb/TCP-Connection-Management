package ir.co.ocs;

import ir.co.ocs.Envoriment.Server;
import ir.co.ocs.Handlers.TimeServerHandler;
import ir.co.ocs.codec.FixedLengthByteArrayFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;

import java.util.concurrent.TimeUnit;

public class Main {


    public static void main(String[] args) {
        ChannelInformation channelInformation = new ChannelInformation();
        Server server = new Server(channelInformation);
        server.create();
        server.addFilter("logging", new LoggingFilter());
        server.addFilter("codec", new ProtocolCodecFilter(new FixedLengthByteArrayFactory()));
        server.setHandler(new TimeServerHandler());
        server.start(8080);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
