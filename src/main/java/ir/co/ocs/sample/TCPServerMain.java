package ir.co.ocs.sample;

import ir.co.ocs.envoriment.server.Server;
import ir.co.ocs.Handlers.ServerHandler;
import ir.co.ocs.envoriment.server.ServerFactory;
import ir.co.ocs.envoriment.server.TCPServer;

import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import ir.co.ocs.socketconfiguration.enums.SocketMode;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.WriteRequestFilter;
import org.apache.mina.filter.firewall.ConnectionThrottleFilter;
import org.apache.mina.filter.util.ReferenceCountingFilter;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TCPServerMain {


    public static void main(String[] args) {
        ServerSocketConfiguration serverSocketConfiguration = new ServerSocketConfiguration();
        serverSocketConfiguration.setPort(8080);
        serverSocketConfiguration.setSocketMode(SocketMode.BOTH);
        serverSocketConfiguration.setReuseAddress(true);
        serverSocketConfiguration.setKeepAlive(true);


        Server server = new TCPServer(serverSocketConfiguration, new ServerFactory());
        server.setHandler(new ServerHandler());
        server.start();
        server.addFilter("executor", new ExecutorFilter(Executors.newFixedThreadPool(4)));
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
