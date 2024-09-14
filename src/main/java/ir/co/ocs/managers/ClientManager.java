package ir.co.ocs.managers;

import ir.co.ocs.envoriment.client.Client;
import lombok.extern.log4j.Log4j;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The {@code ClientManager} class is responsible for managing client connections in a multithreaded environment.
 * It extends the {@code AbstractManager} class to handle a collection of {@code Client} instances and implements
 * the {@code ConnectionManager} interface to manage client connections lifecycle.
 *
 * <p>This class uses an {@code ExecutorService} to manage client connection tasks asynchronously,
 * ensuring that each client runs in its own thread. It supports starting, restarting, and shutting down clients,
 * as well as managing reconnection attempts for clients that lose their connection to a server.</p>
 *
 * <p>Logging is handled via Log4j, providing detailed information about the connection process, including errors
 * and reconnection attempts.</p>
 *
 * @see AbstractManager
 * @see ConnectionManager
 */
@Log4j
public class ClientManager extends AbstractManager<Client> implements ConnectionManager<Client> {
    private final ExecutorService executorService;


    /**
     * Constructs a new {@code ClientManager} with a cached thread pool for managing client connections.
     */
    public ClientManager() {
        executorService = Executors.newCachedThreadPool();
    }

    /**
     * Starts the connection process for the specified {@code Client}. This method submits the client connection
     * task to the {@code ExecutorService}, allowing it to run in a separate thread.
     *
     * @param client the client to start the connection process for
     */
    public void startConnection(Client client) {

        executorService.submit(() -> {
            try {
                manageConnection(client);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    /**
     * Restarts the connection for the specified client, identified by its name. If the client is found, it is restarted.
     *
     * @param identificationName the name of the client to restart
     */
    @Override
    public void restart(String identificationName) {
        Client client = services.get(identificationName);
        if (client != null) {
            client.restart();
        }
    }

    /**
     * Manages the connection lifecycle for a {@code Client}. This method continuously attempts to connect the client
     * to a server, handling connection retries and waiting for the session to close. If the connection is lost,
     * it will retry based on the client configuration. Permanent clients (marked as high priority) will keep retrying indefinitely.
     *
     * @param client the client whose connection to manage
     * @throws InterruptedException if the connection management process is interrupted
     */
    @Override
    public void manageConnection(Client client) throws InterruptedException {
        int retryCount = 0;
        boolean isPermanent = client.getClientConfig().isHighPriority();  // Assume `isHighPriority` is used to mark a client as permanent

        while (client.isActive()) {
            try {
                client.start();  // Attempt to connect to the server
                log.info("Client " + client + " connected successfully.");
                retryCount = 0;
                client.getSession().getCloseFuture().awaitUninterruptibly();  // Wait for the session to close
                log.warn("Client " + client + " connection lost. Retrying...");
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
    /**
     * Shuts down the {@code ClientManager}, stopping all managed clients and gracefully terminating the
     * {@code ExecutorService}. If the executor service fails to shut down within the specified time, it is
     * forced to shut down. Each client is also stopped gracefully.
     */
    @Override
    public void shutdown() {
        log.info("Shutting down ClientManager ExecutorService...");

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                log.warn("ExecutorService did not terminate in the specified time. Forcing shutdown...");
                executorService.shutdownNow();
            }
            services.forEach((name, client) -> {
                try {
                    log.info("Shutting down client: " + client.getIdentification());
                    client.stop();  // Stop each client gracefully
                    log.info("Server " + client.getIdentification() + " successfully shut down.");
                } catch (Exception e) {
                    log.error("Error shutting down client: " + client.getIdentification(), e);
                }
            });
        } catch (InterruptedException e) {
            log.error("Shutdown process interrupted. Forcing shutdown...");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
