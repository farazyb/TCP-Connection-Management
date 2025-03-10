package ir.co.ocs.api.controller;

import ir.co.ocs.api.model.ServiceStatus;
import ir.co.ocs.api.service.ServiceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceControllerTest {

    @Mock
    private ServiceManager serviceManager;

    private ServiceController serviceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serviceController = new ServiceController(serviceManager);
    }

    @Test
    void testGetAllServices() {
        Map<String, ServiceStatus> expectedServices = new HashMap<>();
        expectedServices.put("service1", ServiceStatus.builder()
                .serviceId("service1")
                .serviceType("SERVER")
                .status("RUNNING")
                .build());

        when(serviceManager.getAllServices()).thenReturn(expectedServices);

        ResponseEntity<Map<String, ServiceStatus>> response = serviceController.getAllServices();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedServices, response.getBody());
    }

    @Test
    void testGetServiceStatus() {
        String serviceId = "service1";
        ServiceStatus expectedStatus = ServiceStatus.builder()
                .serviceId(serviceId)
                .serviceType("SERVER")
                .status("RUNNING")
                .build();

        when(serviceManager.getServiceStatus(serviceId)).thenReturn(expectedStatus);

        ResponseEntity<ServiceStatus> response = serviceController.getServiceStatus(serviceId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedStatus, response.getBody());
    }

    @Test
    void testGetNonExistentServiceStatus() {
        String serviceId = "nonexistent";
        when(serviceManager.getServiceStatus(serviceId)).thenReturn(null);

        ResponseEntity<ServiceStatus> response = serviceController.getServiceStatus(serviceId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testStartService() {
        String serviceId = "service1";
        ResponseEntity<Void> response = serviceController.startService(serviceId);

        assertEquals(200, response.getStatusCodeValue());
        verify(serviceManager).startService(serviceId);
    }

    @Test
    void testStopService() {
        String serviceId = "service1";
        ResponseEntity<Void> response = serviceController.stopService(serviceId);

        assertEquals(200, response.getStatusCodeValue());
        verify(serviceManager).stopService(serviceId);
    }

    @Test
    void testRestartService() {
        String serviceId = "service1";
        ResponseEntity<Void> response = serviceController.restartService(serviceId);

        assertEquals(200, response.getStatusCodeValue());
        verify(serviceManager).restartService(serviceId);
    }
} 