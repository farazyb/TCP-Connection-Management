package ir.co.ocs.api.controller;

import ir.co.ocs.envoriment.client.Client;
import ir.co.ocs.managers.ClientManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing TCP client connections.
 * Provides endpoints for starting, stopping, restarting, and monitoring client connections.
 */
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client Management", description = "APIs for managing TCP client connections")
public class ClientController {
    private final ClientManager clientManager;

    public ClientController(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    /**
     * Starts a new TCP client connection.
     * @param client Client configuration including host, port, and other settings
     * @return Success message if client started successfully
     */
    @Operation(summary = "Start a new client connection", 
              description = "Initializes and starts a new TCP client with the provided configuration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client started successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid client configuration")
    })
    @PostMapping
    public ResponseEntity<String> startClient(@RequestBody Client client) {
        clientManager.startConnection(client);
        return ResponseEntity.ok("Client started successfully");
    }

    /**
     * Stops an existing client connection.
     * @param id Unique identifier of the client to stop
     * @return Success message if client stopped successfully
     */
    @Operation(summary = "Stop a client connection",
              description = "Gracefully stops and disconnects the specified client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client stopped successfully"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> stopClient(
            @Parameter(description = "Unique identifier of the client to stop") 
            @PathVariable String id) {
        clientManager.stop(id);
        return ResponseEntity.ok("Client stopped successfully");
    }

    /**
     * Restarts an existing client connection.
     * @param id Unique identifier of the client to restart
     * @return Success message if client restarted successfully
     */
    @Operation(summary = "Restart a client connection",
              description = "Stops and restarts the specified client with its original configuration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client restarted successfully"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PutMapping("/{id}/restart")
    public ResponseEntity<String> restartClient(
            @Parameter(description = "Unique identifier of the client to restart")
            @PathVariable String id) {
        clientManager.restart(id);
        return ResponseEntity.ok("Client restarted successfully");
    }

    /**
     * Lists all active client connections.
     * @return Map of client IDs to their corresponding Client objects
     */
    @Operation(summary = "List all clients",
              description = "Returns a list of all registered client connections")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved client list")
    })
    @GetMapping
    public ResponseEntity<Map<String, Client>> listClients() {
        return ResponseEntity.ok(clientManager.getServices());
    }

    /**
     * Gets the current status of a client connection.
     * @param id Unique identifier of the client to check
     * @return Boolean indicating if the client is active
     */
    @Operation(summary = "Get client status",
              description = "Returns the current active status of the specified client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved client status")
    })
    @GetMapping("/{id}/status")
    public ResponseEntity<Boolean> getClientStatus(
            @Parameter(description = "Unique identifier of the client to check")
            @PathVariable String id) {
        Client client = clientManager.getServices().get(id);
        return ResponseEntity.ok(client != null && client.isActive());
    }
} 