package ir.co.ocs;

import org.apache.mina.core.session.IoSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionManagerTest {

    @Mock
    private IoSession mockSession;

    private SessionManager sessionManager;
    private static final int TIMEOUT_SECONDS = 2;
    private static final boolean PERMANENT = false;
    private static final int CHECK_INTERVAL = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessionManager = new SessionManager(TIMEOUT_SECONDS, PERMANENT, CHECK_INTERVAL);
        when(mockSession.getId()).thenReturn(1L);
        when(mockSession.isConnected()).thenReturn(true);
        when(mockSession.isClosing()).thenReturn(false);
        when(mockSession.getLastIoTime()).thenReturn(System.currentTimeMillis());
    }

    @Test
    void testAddSession() {
        long sessionId = sessionManager.add(mockSession);
        assertEquals(1L, sessionId);
        assertNotNull(sessionManager.get(sessionId));
        assertEquals(mockSession, sessionManager.get(sessionId));
    }

    @Test
    void testRemoveSession() {
        sessionManager.add(mockSession);
        sessionManager.remove(1L);
        assertNull(sessionManager.get(1L));
        verify(mockSession).closeNow();
    }

    @Test
    void testGetNonExistentSession() {
        assertNull(sessionManager.get(999L));
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testSessionTimeout() throws InterruptedException {
        // Set up a session with old lastIoTime
        when(mockSession.getLastIoTime()).thenReturn(System.currentTimeMillis() - (TIMEOUT_SECONDS + 1) * 1000);
        sessionManager.add(mockSession);

        // Wait for timeout check
        Thread.sleep(3000);

        // Verify session was removed
        assertNull(sessionManager.get(1L));
        verify(mockSession).closeNow();
    }

    @Test
    void testPermanentSessionNoTimeout() {
        // Create a new session manager with permanent sessions
        SessionManager permanentSessionManager = new SessionManager(TIMEOUT_SECONDS, true, CHECK_INTERVAL);
        
        // Set up a session with old lastIoTime
        when(mockSession.getLastIoTime()).thenReturn(System.currentTimeMillis() - (TIMEOUT_SECONDS + 1) * 1000);
        permanentSessionManager.add(mockSession);

        // Verify session is still present
        assertNotNull(permanentSessionManager.get(1L));
        verify(mockSession, never()).closeNow();
    }

    @Test
    void testRemoveDisconnectedSession() {
        when(mockSession.isConnected()).thenReturn(false);
        sessionManager.add(mockSession);
        sessionManager.remove(1L);
        assertNull(sessionManager.get(1L));
        verify(mockSession, never()).closeNow();
    }
} 