package ir.co.ocs.api.controller;

import ir.co.ocs.envoriment.server.Server;
import ir.co.ocs.managers.ServerManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing TCP server connections.
 * Provides endpoints for starting, stopping, restarting, and monitoring server connections.
 */
@RestController
@RequestMapping("/api/servers")
@Tag(name = "Server Management", description = "APIs for managing TCP server connections")
public class ServerController {
    private final ServerManager serverManager;

    public ServerController(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    /**
     * Starts a new TCP server.
     * @param server Server configuration including port and other settings
     * @return Success message if server started successfully
     */
    @Operation(summary = "Start a new server", 
              description = "Initializes and starts a new TCP server with the provided configuration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Server started successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid server configuration")
    })
    @PostMapping
    public ResponseEntity<String> startServer(@RequestBody Server server) {
        serverManager.startConnection(server);
        return ResponseEntity.ok("Server started successfully");
    }

    /**
     * Stops an existing server.
     * @param id Unique identifier of the server to stop
     * @return Success message if server stopped successfully
     */
    @Operation(summary = "Stop a server",
              description = "Gracefully stops and unbinds the specified server")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Server stopped successfully"),
        @ApiResponse(responseCode = "404", description = "Server not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> stopServer(
            @Parameter(description = "Unique identifier of the server to stop") 
            @PathVariable String id) {
        serverManager.stopConnection(serverManager.getServices().get(id));
        return ResponseEntity.ok("Server stopped successfully");
    }

    /**
     * Restarts an existing server.
     * @param id Unique identifier of the server to restart
     * @return Success message if server restarted successfully
     */
    @Operation(summary = "Restart a server",
              description = "Stops and restarts the specified server with its original configuration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Server restarted successfully"),
        @ApiResponse(responseCode = "404", description = "Server not found")
    })
    @PutMapping("/{id}/restart")
    public ResponseEntity<String> restartServer(
            @Parameter(description = "Unique identifier of the server to restart")
            @PathVariable String id) {
        serverManager.restart(serverManager.getServices().get(id));
        return ResponseEntity.ok("Server restarted successfully");
    }

    /**
     * Lists all active server connections.
     * @return Map of server IDs to their corresponding Server objects
     */
    @Operation(summary = "List all servers",
              description = "Returns a list of all registered server connections")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved server list")
    })
    @GetMapping
    public ResponseEntity<Map<String, Server>> listServers() {
        return ResponseEntity.ok(serverManager.getServices());
    }

    /**
     * Gets the current status of a server connection.
     * @param id Unique identifier of the server to check
     * @return Boolean indicating if the server is active
     */
    @Operation(summary = "Get server status",
              description = "Returns the current active status of the specified server")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved server status")
    })
    @GetMapping("/{id}/status")
    public ResponseEntity<Boolean> getServerStatus(
            @Parameter(description = "Unique identifier of the server to check")
            @PathVariable String id) {
        Server server = serverManager.getServices().get(id);
        return ResponseEntity.ok(server != null && server.isActive());
    }
} 