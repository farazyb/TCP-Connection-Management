package ir.co.ocs.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceStatus {
    private String serviceId;
    private String serviceType; // SERVER or CLIENT
    private String status; // RUNNING, STOPPED, ERROR
    private String host;
    private int port;
    private long uptime;
    private int activeConnections;
    private String lastError;
} 