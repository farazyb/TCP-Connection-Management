package ir.co.ocs.sample;

import ir.co.ocs.Handlers.ClientHandler;
import ir.co.ocs.envoriment.client.ClientFactory;
import ir.co.ocs.envoriment.client.TCPClient;
import ir.co.ocs.envoriment.client.ClientManager;
import ir.co.ocs.socketconfiguration.ClientSocketConfiguration;

public class TCPClientMain {
    public static void main(String[] args) {
        ClientSocketConfiguration clientSocketConfiguration = new ClientSocketConfiguration();
        clientSocketConfiguration.setHost("localhost");
        clientSocketConfiguration.setPort(8080);
        clientSocketConfiguration.setPermanent(true);
        clientSocketConfiguration.setMaxRetries(10);
        clientSocketConfiguration.setRetryInterval(10000);
        TCPClient client = new TCPClient(clientSocketConfiguration, new ClientFactory());
        int[] array =new int[]{1,2,3};
        client.setHandler(new ClientHandler(array));
        ClientManager clientManager=new ClientManager();

        clientManager.add(client);
    }
}
