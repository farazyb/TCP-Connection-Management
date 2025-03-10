package ir.co.ocs.envoriment.networkchannel;

/**
 * Interface defining the basic lifecycle operations for network services.
 * This interface provides a standard contract for service initialization,
 * startup, shutdown, and status checking.
 *
 * @author OCS Team
 * @version 1.0
 */
public interface ServiceLifecycle {
    /**
     * Initializes the service with necessary configurations and resources.
     * This method should be called before starting the service.
     *
     * @throws IllegalStateException if the service is already initialized
     */
    void initialize();

    /**
     * Starts the service and begins processing.
     * This method should be called after initialization.
     *
     * @return The service instance for method chaining
     * @throws IllegalStateException if the service is not initialized or is already running
     */
    NetworkChannel start();

    /**
     * Stops the service and releases resources.
     * This method should be called when the service is no longer needed.
     *
     * @throws IllegalStateException if the service is not running
     */
    void stop();

    /**
     * Checks if the service is currently running.
     *
     * @return true if the service is active, false otherwise
     */
    boolean isRunning();
}
