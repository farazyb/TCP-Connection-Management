package ir.co.ocs.envoriment.networkchannel;

import ir.co.ocs.socketconfiguration.SocketConfigurationInterface;

import java.util.concurrent.CompletableFuture;

/**
 * Interface defining the core functionality for network channel operations.
 * This interface provides the basic contract for network communication channels,
 * including lifecycle management and configuration access.
 *
 * @author OCS Team
 * @version 1.0
 */
public interface NetworkChannel {
    /**
     * Starts the network channel.
     * This method initiates the channel's operation and begins accepting/establishing connections.
     *
     * @return The network channel instance for method chaining
     */
    NetworkChannel start();

    /**
     * Starts the network channel asynchronously.
     * This method initiates the channel's operation and returns a future that completes when the channel is ready.
     *
     * @return A CompletableFuture that completes when the channel is ready
     */
    CompletableFuture<Boolean> startService();

    /**
     * Stops the network channel.
     * This method gracefully terminates the channel's operation and closes all active connections.
     */
    void stop();

    /**
     * Restarts the network channel.
     * This method performs a complete stop and start sequence to reset the channel's state.
     *
     * @return The network channel instance for method chaining
     */
    NetworkChannel restart();

    /**
     * Checks if the network channel is currently active.
     *
     * @return true if the channel is active, false otherwise
     */
    boolean isActive();

    /**
     * Gets the unique identification of the network channel.
     *
     * @return The channel's identification string
     */
    String getIdentification();

    /**
     * Gets the configuration for this network channel.
     *
     * @return The socket configuration interface
     */
    SocketConfigurationInterface getConfiguration();
}
