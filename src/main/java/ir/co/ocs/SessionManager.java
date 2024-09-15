package ir.co.ocs;

import org.apache.mina.core.session.IoSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SessionManager {
    private final ConcurrentHashMap<Long, IoSession> sessions;
    private final Executor executor;
    private final int timeout; // Timeout in seconds
    private final boolean permanent;

    public SessionManager(int timeout, boolean permanent) {
        this.sessions = new ConcurrentHashMap<>();
        this.executor = Executors.newCachedThreadPool();
        this.timeout = timeout;
        this.permanent = permanent;
        manageTimeout();
    }

    public long add(IoSession ioSession) {
        sessions.put(ioSession.getId(), ioSession);
        return ioSession.getId();
    }

    public void remove(Long sessionId) {
        IoSession session = sessions.remove(sessionId);
        if (session != null && session.isConnected()) {
            session.closeNow(); // Closes the session if still connected
        }
    }

    public IoSession get(Long sessionId) {
        return sessions.get(sessionId);
    }

    private void manageTimeout() {
        executor.execute(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(10); // Check every 10 seconds
                    long currentTime = System.currentTimeMillis();
                    sessions.forEach((id, session) -> {
                        if (!session.isClosing() && session.isConnected()) {
                            long idleTime = currentTime - session.getLastIoTime();
                            if (idleTime >= timeout * 1000 && !isPermanent()) {
                                remove(id);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private boolean isPermanent() {
        return permanent;
    }

}
