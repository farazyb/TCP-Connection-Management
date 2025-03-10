package ir.co.ocs.managers;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;
import ir.co.ocs.envoriment.server.Server;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
@Component
public class ServerManager {
    private final ConcurrentMap<String, Server> servers = new ConcurrentHashMap<>();

    /**
     * Starts the connection process for the specified {@code Server}. This method calls the {@code startService()} method
     * on the server instance, and logs the server's identification and port number it is listening on.
     *
     * @param server the server to start the connection process for
     */
    public void startConnection(NetworkChannel server) {
        if (server.isActive()) {
            throw new IllegalStateException("Server is already active");
        }
        server.startService();
    }

    public void stopConnection(NetworkChannel server) {
        if (server.isActive()) {
            server.stop();
        }
    }

    public void restart(NetworkChannel server) {
        if (server.isActive()) {
            server.restart();
        }
    }

    /**
     * Shuts down all managed servers. This method iterates over all the {@code Server} instances managed by
     * the {@code ServerManager}, and for each one, it attempts to stop the server gracefully by calling the
     * {@code stop()} method. Detailed logging is performed for each server to track the shutdown process.
     *
     * <p>In case of any exceptions during the shutdown process, they are caught and logged.</p>
     */
    public void shutdown() {
        servers.values().forEach(this::stopConnection);
        servers.clear();
    }

    public ConcurrentMap<String, Server> getServices() {
        return servers;
    }
}
