package ir.co.ocs.managers;

import ir.co.ocs.envoriment.server.Server;
import lombok.extern.log4j.Log4j;

/**
 * The {@code ServerManager} class is responsible for managing a collection of {@code Server} instances.
 * It extends the {@code AbstractManager} class, inheriting the ability to manage multiple servers and
 * providing methods to start and shut down each server.
 *
 * <p>This class handles starting each server's connection and shutting down all servers gracefully,
 * ensuring that resources are properly released and each server is stopped cleanly.</p>
 *
 * <p>Logging is handled via Log4j, providing detailed information about server operations.</p>
 *
 * @see AbstractManager
 */
@Log4j
public class ServerManager extends AbstractManager<Server> {
    /**
     * Starts the connection process for the specified {@code Server}. This method calls the {@code start()} method
     * on the server instance, and logs the server's identification and port number it is listening on.
     *
     * @param server the server to start the connection process for
     */
    @Override
    public void startConnection(Server server) {
        server.start();
        log.info("Server " + server.getIdentification() + " Starts Listening on port:" + server.getServerConfig().getPort());
    }

    public void restart(String identificationName) {
        Server networkChannel = remove(identificationName);
        networkChannel.restart();
        add(networkChannel);

    }

    /**
     * Shuts down all managed servers. This method iterates over all the {@code Server} instances managed by
     * the {@code ServerManager}, and for each one, it attempts to stop the server gracefully by calling the
     * {@code stop()} method. Detailed logging is performed for each server to track the shutdown process.
     *
     * <p>In case of any exceptions during the shutdown process, they are caught and logged.</p>
     */
    @Override
    public void shutdown() {
        log.info("Shutting down all servers...");
        services.forEach((name, server) -> {
            try {
                log.info("Shutting down server: " + server.getIdentification());
                server.stop();  // Stop each server gracefully
                log.info("Server " + server.getIdentification() + " successfully shut down.");
            } catch (Exception e) {
                log.error("Error shutting down server: " + server.getIdentification(), e);
            }
        });
        log.info("All servers shut down successfully.");
    }

}
