package managers;

import ir.co.ocs.envoriment.server.Server;
import ir.co.ocs.managers.ServerManager;
import ir.co.ocs.socketconfiguration.BaseTcpSocketConfiguration;
import ir.co.ocs.socketconfiguration.ServerSocketConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ServerManagerTest {
    private ServerManager serverManager;
    private Server mockServer;

    @BeforeEach
    void setUp() {
        serverManager = new ServerManager(); // Create instance of ServerManager
        mockServer = mock(Server.class);     // Mock the Server object
        ServerSocketConfiguration mockConfig = mock(ServerSocketConfiguration.class); // Mock server config
        BaseTcpSocketConfiguration mockBaseConfig = mock(BaseTcpSocketConfiguration.class);

        // Mock behavior for the Server
        when(mockServer.getServerConfig()).thenReturn(mockConfig);
        when(mockServer.getIdentification()).thenReturn("Server1");
        when(mockServer.getConfiguration()).thenReturn(mockBaseConfig);
        when(mockBaseConfig.getChannelIdentificationName()).thenReturn("Server1");
        when(mockConfig.getPort()).thenReturn(8080);
    }

    @Test
    void testStartConnection() {
        serverManager.startConnection(mockServer);
        verify(mockServer).start(); // Ensure start() is called on the server
    }

    @Test
    void testStopConnection() {
        serverManager.add(mockServer); // Add mock server
        serverManager.stop("Server1"); // Stop server
        verify(mockServer).stop();     // Verify that stop() was called
    }

    @Test
    void testRestart() {
        serverManager.add(mockServer);
        serverManager.restart("Server1");  // Restart server
        verify(mockServer).restart();      // Verify that restart() is called
    }

    @Test
    void testShutdown() {
        serverManager.add(mockServer);  // Add mock server to ServerManager
        serverManager.shutdown();       // Call shutdown method
        verify(mockServer).stop();      // Verify stop() was called on the server
    }
}
