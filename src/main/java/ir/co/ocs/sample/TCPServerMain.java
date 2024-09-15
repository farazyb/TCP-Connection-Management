package ir.co.ocs.sample;

import ir.co.ocs.envoriment.server.Server;
import ir.co.ocs.Handlers.ServerHandler;
import ir.co.ocs.envoriment.server.ServerFactory;
import ir.co.ocs.envoriment.server.TCPServer;

import ir.co.ocs.managers.ServerManager;
import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import ir.co.ocs.socketconfiguration.enums.SocketMode;
import lombok.extern.log4j.Log4j;
import org.apache.mina.filter.executor.ExecutorFilter;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j
public class TCPServerMain {


    public static void main(String[] args) {
        ServerSocketConfiguration serverSocketConfiguration = new ServerSocketConfiguration();
        serverSocketConfiguration.setPort(8080);
        serverSocketConfiguration.setSocketMode(SocketMode.BOTH);
        serverSocketConfiguration.setReuseAddress(true);
        serverSocketConfiguration.setKeepAlive(true);
        serverSocketConfiguration.setChannelIdentificationName("Test_Server");


        Server server = new TCPServer(serverSocketConfiguration, new ServerFactory());
        server.setHandler(new ServerHandler());
        server.addFilter("executor", new ExecutorFilter(Executors.newFixedThreadPool(4)));
        ServerManager serverManager = new ServerManager();
        serverManager.add(server);
//        timeout();
//        serverManager.stop(server.getIdentification());
        timeout();
        serverManager.restart(server.getIdentification());
        timeout();
        serverManager.shutdown();
    }

    private static void timeout() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
