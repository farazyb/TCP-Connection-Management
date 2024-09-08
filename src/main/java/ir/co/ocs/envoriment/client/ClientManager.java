package ir.co.ocs.envoriment.client;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;
import ir.co.ocs.managers.AbstractManager;
import ir.co.ocs.managers.ConnectionManager;
import lombok.extern.log4j.Log4j;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j
public class ClientManager extends AbstractManager<Client> implements ConnectionManager<Client> {
    private final ExecutorService executorService;

    public ClientManager() {
        executorService = Executors.newCachedThreadPool();
    }

    public void startConnection(Client client) {
        executorService.submit(() -> {
            try {
                manageConnection(client);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    

    @Override
    public void manageConnection(Client client) throws InterruptedException {
        int retryCount = 0;
        boolean isPermanent = client.getClientConfig().isHighPriority();  // Assume `isHighPriority` is used to mark a client as permanent

        while (!client.shouldStop()) {
            try {
                client.start();  // Attempt to connect to the server
                log.info("Client " + client + " connected successfully.");
                client.getSession().getCloseFuture().awaitUninterruptibly();  // Wait for the session to close
                System.out.println("Client " + client + " connection lost. Retrying...");
            } catch (Exception e) {
                log.error("Client " + client + " connection failed. Retrying...");
                log.trace(e.getMessage());
            }

            // Handle retry logic
            retryCount++;
            if (!isPermanent && retryCount >= client.getClientConfig().getMaxRetries()) {
                log.error("Max retry attempts reached for client " + client + ". Giving up.");
                break;
            }

            TimeUnit.MILLISECONDS.sleep(client.getClientConfig().getRetryInterval());  // Wait before retrying
        }
    }

}
