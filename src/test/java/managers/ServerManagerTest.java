package managers;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;
import ir.co.ocs.socketconfiguration.SocketConfigurationInterface;
import ir.co.ocs.managers.ServerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ServerManagerTest {
    private ServerManager serverManager;

    @Mock
    private NetworkChannel mockServer;

    @Mock
    private SocketConfigurationInterface mockConfig;

    CompletableFuture<Boolean> future;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serverManager = new ServerManager();
        future = new CompletableFuture<>();
        
        when(mockServer.getConfiguration()).thenReturn(mockConfig);
        when(mockServer.startService()).thenReturn(future);
        when(mockServer.isActive()).thenReturn(false);
        when(mockServer.getIdentification()).thenReturn("Server1");
    }

    @Test
    void testStartConnection() {
        when(mockServer.isActive()).thenReturn(false);
        serverManager.startConnection(mockServer);
        verify(mockServer).startService();
    }

    @Test
    void testStartActiveServer() {
        when(mockServer.isActive()).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> serverManager.startConnection(mockServer));
    }

    @Test
    void testStopConnection() {
        when(mockServer.isActive()).thenReturn(true);
        serverManager.stopConnection(mockServer);
        verify(mockServer).stop();
    }

    @Test
    void testRestart() {
        when(mockServer.isActive()).thenReturn(true);
        serverManager.restart(mockServer);
        verify(mockServer).restart();
    }

    @Test
    void testShutdown() {
        serverManager.shutdown();
        verify(mockServer, never()).stop();
    }
}
