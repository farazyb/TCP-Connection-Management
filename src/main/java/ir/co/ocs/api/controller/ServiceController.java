package ir.co.ocs.api.controller;

import ir.co.ocs.api.model.ServiceStatus;
import ir.co.ocs.api.service.ServiceManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private final ServiceManager serviceManager;

    public ServiceController(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @GetMapping
    public ResponseEntity<Map<String, ServiceStatus>> getAllServices() {
        return ResponseEntity.ok(serviceManager.getAllServices());
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceStatus> getServiceStatus(@PathVariable String serviceId) {
        ServiceStatus status = serviceManager.getServiceStatus(serviceId);
        return status != null ? ResponseEntity.ok(status) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{serviceId}/start")
    public ResponseEntity<Void> startService(@PathVariable String serviceId) {
        serviceManager.startService(serviceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{serviceId}/stop")
    public ResponseEntity<Void> stopService(@PathVariable String serviceId) {
        serviceManager.stopService(serviceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{serviceId}/restart")
    public ResponseEntity<Void> restartService(@PathVariable String serviceId) {
        serviceManager.restartService(serviceId);
        return ResponseEntity.ok().build();
    }
} 