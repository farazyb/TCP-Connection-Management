package ir.co.ocs.sample;

import ir.co.ocs.Handlers.ClientHandler;
import ir.co.ocs.envoriment.client.ClientFactory;
import ir.co.ocs.envoriment.client.TCPClient;
import ir.co.ocs.managers.ClientManager;
import ir.co.ocs.socketconfiguration.ClientSocketConfiguration;

import java.util.concurrent.TimeUnit;

public class TCPClientMain {
    public static void main(String[] args) {
        TCPClient client = getTcpClient();
        int[] array =new int[]{1,2,3};
        client.setHandler(new ClientHandler(array));
        ClientManager clientManager=new ClientManager();

        clientManager.add(client);
       timeOut();
        clientManager.restart(client.getIdentification());

    }

    private static void timeOut() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static TCPClient getTcpClient() {
        ClientSocketConfiguration clientSocketConfiguration = new ClientSocketConfiguration();
        clientSocketConfiguration.setHost("localhost");
        clientSocketConfiguration.setPort(8080);
        clientSocketConfiguration.setPermanent(true);
        clientSocketConfiguration.setMaxRetries(10);
        clientSocketConfiguration.setRetryInterval(10000);
        clientSocketConfiguration.setChannelIdentificationName("Test");
        return new TCPClient(clientSocketConfiguration, new ClientFactory());
    }
}
