package ir.co.ocs.api.service;

import ir.co.ocs.api.model.ServiceStatus;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.InetSocketAddress;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServiceManagerTest {

    @Mock
    private NioSocketAcceptor mockAcceptor;

    @Mock
    private NioSocketConnector mockConnector;

    @Mock
    private IoSession mockSession;

    private ServiceManager serviceManager;
    private ArgumentCaptor<IoServiceListener> listenerCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serviceManager = new ServiceManager();
        listenerCaptor = ArgumentCaptor.forClass(IoServiceListener.class);
    }

    @Test
    void testRegisterServer() {
        String serviceId = "server1";
        String host = "localhost";
        int port = 8080;

        serviceManager.registerServer(serviceId, host, port, mockAcceptor);

        ServiceStatus status = serviceManager.getServiceStatus(serviceId);
        assertNotNull(status);
        assertEquals("SERVER", status.getServiceType());
        assertEquals(host, status.getHost());
        assertEquals(port, status.getPort());
        assertEquals("STOPPED", status.getStatus());

        verify(mockAcceptor).addListener(any(IoServiceListener.class));
    }

    @Test
    void testRegisterClient() {
        String serviceId = "client1";
        String host = "localhost";
        int port = 8080;

        serviceManager.registerClient(serviceId, host, port, mockConnector);

        ServiceStatus status = serviceManager.getServiceStatus(serviceId);
        assertNotNull(status);
        assertEquals("CLIENT", status.getServiceType());
        assertEquals(host, status.getHost());
        assertEquals(port, status.getPort());
        assertEquals("STOPPED", status.getStatus());

        verify(mockConnector).addListener(any(IoServiceListener.class));
    }

    @Test
    void testStartServer() throws Exception {
        String serviceId = "server1";
        serviceManager.registerServer(serviceId, "localhost", 8080, mockAcceptor);
        when(mockAcceptor.isActive()).thenReturn(false);

        serviceManager.startService(serviceId);

        verify(mockAcceptor).bind(any(InetSocketAddress.class));
    }

    @Test
    void testStartClient() throws Exception {
        String serviceId = "client1";
        serviceManager.registerClient(serviceId, "localhost", 8080, mockConnector);
        when(mockConnector.isActive()).thenReturn(false);

        serviceManager.startService(serviceId);

        verify(mockConnector).connect(any(InetSocketAddress.class));
    }

    @Test
    void testStopServer() {
        String serviceId = "server1";
        serviceManager.registerServer(serviceId, "localhost", 8080, mockAcceptor);
        when(mockAcceptor.isActive()).thenReturn(true);

        serviceManager.stopService(serviceId);

        verify(mockAcceptor).unbind();
    }

    @Test
    void testStopClient() {
        String serviceId = "client1";
        serviceManager.registerClient(serviceId, "localhost", 8080, mockConnector);
        when(mockConnector.isActive()).thenReturn(true);

        serviceManager.stopService(serviceId);

        verify(mockConnector).dispose();
    }

    @Test
    void testRestartService() throws Exception {
        String serviceId = "server1";
        serviceManager.registerServer(serviceId, "localhost", 8080, mockAcceptor);
        when(mockAcceptor.isActive()).thenReturn(true, false);

        serviceManager.restartService(serviceId);

        verify(mockAcceptor).unbind();
        verify(mockAcceptor).bind(any(InetSocketAddress.class));
    }

    @Test
    void testGetAllServices() {
        serviceManager.registerServer("server1", "localhost", 8080, mockAcceptor);
        serviceManager.registerClient("client1", "localhost", 8081, mockConnector);

        Map<String, ServiceStatus> services = serviceManager.getAllServices();

        assertEquals(2, services.size());
        assertTrue(services.containsKey("server1"));
        assertTrue(services.containsKey("client1"));
    }

    @Test
    void testSessionManagement() throws Exception {
        String serviceId = "server1";
        serviceManager.registerServer(serviceId, "localhost", 8080, mockAcceptor);

        // Capture the listener
        verify(mockAcceptor).addListener(listenerCaptor.capture());
        IoServiceListener listener = listenerCaptor.getValue();

        // Simulate session creation
        listener.sessionCreated(mockSession);
        ServiceStatus status = serviceManager.getServiceStatus(serviceId);
        assertEquals(1, status.getActiveConnections());

        // Simulate session closure
        listener.sessionClosed(mockSession);
        status = serviceManager.getServiceStatus(serviceId);
        assertEquals(0, status.getActiveConnections());
    }
} 