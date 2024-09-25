package ir.co.ocs.Handlers;

import ir.co.ocs.ProcessHandler;
import ir.co.ocs.Router;
import ir.co.ocs.SessionManager;
import ir.co.ocs.SessionStore;
import ir.co.ocs.connections.DataInformation;
import lombok.extern.log4j.Log4j;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import org.apache.mina.filter.FilterEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;


@Log4j
public abstract class NetworkChannelHandler extends IoHandlerAdapter {
    private final SessionStore sessionStore;

    public NetworkChannelHandler() {
        log.info("new Instance of " + this.getClass().getName() + "created");
        this.sessionStore = SessionStore.getInstance();
    }

    @Override
    public final void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
    }

    @Override
    public final void sessionOpened(IoSession session) throws Exception {
        sessionStore.addSession(session.getId(), session);
        log.info("Session stored: " + session.getId());
    }

    @Override
    public final void sessionCreated(IoSession session) throws Exception {
        log.info("ECHO THREAD : {" + Thread.currentThread().getName() + "}");
        log.info("Adding channel attribute");
        log.info("Session TimeOut : " + session.getConfig().getWriteTimeout());
    }

    @Override
    public final void messageReceived(IoSession session, Object message) throws Exception {
        readMessage(new DataInformation(session.getId(), (byte[]) message));

    }

    public abstract void readMessage(DataInformation dataInformation);


    @Override
    public final void event(IoSession session, FilterEvent event) throws Exception {

    }

    @Override
    public final void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        if (status == IdleStatus.BOTH_IDLE) {
            log.error("No response sent for 60 seconds, closing session : " + session.getId());
            session.closeNow();  // Close the session if it's idle for more than 60 seconds
        }
    }

    @Override
    public final void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        closeSession(session);
    }

    @Override
    public final void messageSent(IoSession session, Object message) throws Exception {
    }

    @Override
    public final void sessionClosed(IoSession session) throws Exception {
        log.info("Session closed: " + session.getId());
        // Remove the session from the map
        sessionStore.removeSession(session.getId());
        log.info("Session removed: " + session.getId());
    }

    private  void closeSession(IoSession session) {
        session.closeNow();
        sessionStore.removeSession(session.getId());
        log.info("Session forcibly closed and removed: " + session.getId());
    }
}
