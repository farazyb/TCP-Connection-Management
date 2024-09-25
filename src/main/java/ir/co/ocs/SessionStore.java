package ir.co.ocs;

import org.apache.mina.core.session.IoSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStore {

    // Singleton instance
    private static final SessionStore INSTANCE = new SessionStore();

    // Thread-safe map to store active sessions
    private final ConcurrentHashMap<Long, IoSession> activeSessions = new ConcurrentHashMap<>();

    // Private constructor to prevent instantiation
    private SessionStore() {
    }

    // Method to get the singleton instance
    public static SessionStore getInstance() {
        return INSTANCE;
    }

    // Add a session to the store
    public void addSession(Long sessionId, IoSession session) {
        activeSessions.put(sessionId, session);
    }

    // Remove a session from the store
    public void removeSession(Long sessionId) {
        activeSessions.remove(sessionId);
    }

    // Retrieve a session from the store
    public IoSession getSession(Long sessionId) {
        return activeSessions.get(sessionId);
    }

    // Check if a session exists
    public boolean sessionExists(Long sessionId) {
        return activeSessions.containsKey(sessionId);
    }

    // Get all active session IDs
    public Set<Long> getAllSessionIds() {
        return activeSessions.keySet();
    }
}
