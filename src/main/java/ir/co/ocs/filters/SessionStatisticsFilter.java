package ir.co.ocs.filters;

import lombok.extern.log4j.Log4j;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Log4j
public class SessionStatisticsFilter extends IoFilterAdapter {
    private final AtomicInteger messagesRead = new AtomicInteger();
    private final AtomicInteger messagesWritten = new AtomicInteger();
    private final AtomicLong idleReadTime = new AtomicLong();
    private final AtomicLong idleWriteTime = new AtomicLong();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SessionStatisticsFilter() {
        scheduler.scheduleAtFixedRate(this::logStatistics, 1, 10, TimeUnit.SECONDS);
    }

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        messagesRead.incrementAndGet();
        super.messageReceived(nextFilter, session, message);
    }

    @Override
    public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        messagesWritten.incrementAndGet();
        super.messageSent(nextFilter, session, writeRequest);
    }

    @Override
    public void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
        if (status == IdleStatus.READER_IDLE) {
            idleReadTime.addAndGet(System.currentTimeMillis() - session.getLastReadTime());
        } else if (status == IdleStatus.WRITER_IDLE) {
            idleWriteTime.addAndGet(System.currentTimeMillis() - session.getLastWriteTime());
        }
        super.sessionIdle(nextFilter, session, status);
    }

    private void logStatistics() {
        log.info("Messages Read: " + messagesRead.getAndSet(0));
        log.info("Messages Written: " + messagesWritten.getAndSet(0));
        log.info("Idle Read Time (ms): " + idleReadTime.getAndSet(0));
        log.info("Idle Write Time (ms): " + idleWriteTime.getAndSet(0));
    }

    @Override
    public void destroy() throws Exception {
        scheduler.shutdown();
        super.destroy();
    }
}
