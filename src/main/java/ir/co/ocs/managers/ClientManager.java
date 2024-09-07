package ir.co.ocs.managers;

import ir.co.ocs.envoriment.client.Client;
import ir.co.ocs.envoriment.networkchannel.NetworkChannel;
import lombok.extern.log4j.Log4j;


import java.util.concurrent.TimeUnit;

@Log4j
public class ClientManager extends AbstractManager {

    @Override
    protected void manageConnection(NetworkChannel networkChannel) throws InterruptedException {
        if (networkChannel instanceof Client client) {
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
        } else {
            throw new InterruptedException("Network channel must be instance of Client");
        }
    }

}
