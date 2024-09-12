package ir.co.ocs.sample;

import ir.co.ocs.envoriment.server.Server;
import ir.co.ocs.Handlers.ServerHandler;
import ir.co.ocs.envoriment.server.ServerFactory;
import ir.co.ocs.envoriment.server.TCPServer;

import ir.co.ocs.managers.ServerManager;
import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import ir.co.ocs.socketconfiguration.enums.SocketMode;
import org.apache.mina.filter.executor.ExecutorFilter;

import java.util.concurrent.Executors;

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

//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//            serverManager.stop(server.getIdentification());
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        server = new TCPServer(serverSocketConfiguration, new ServerFactory());
//        server.setHandler(new ServerHandler());
//        server.addFilter("executor", new ExecutorFilter(Executors.newFixedThreadPool(4)));
//        serverManager.add(server);
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        serverManager.stop(server.getIdentification());
    }
}
