package ir.co.ocs.Handlers;

import ir.co.ocs.ChannelInformation;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public abstract class NetWorkChannelHandler extends IoHandlerAdapter {
    private final Logger logger;
    private ChannelInformation channelInformation;

    public NetWorkChannelHandler() {
        logger = LoggerFactory.getLogger(this.getClass().getName());
    }

    public void setChannelInformation(ChannelInformation channelInformation) {
        this.channelInformation = channelInformation;
    }

    @Override
    public final void sessionCreated(IoSession session) throws Exception {
        logger.info(" ECHO THREAD : {}", Thread.currentThread().getName());
        session.setAttribute("Channel", this.channelInformation);
    }

    @Override
    public final void messageReceived(IoSession session, Object message) throws Exception {

        String str = message.toString();
        if (str.trim().equalsIgnoreCase("quit")) {
            session.closeOnFlush();
            return;
        }

        Date date = new Date();
        session.write(date.toString().getBytes(StandardCharsets.UTF_8));
        System.out.println("Message written...");
    }

    @Override
    public void event(IoSession session, FilterEvent event) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IDLE " + session.getIdleCount(status));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info("Message : {} wrote for Session : {}", message, session.getLocalAddress());
    }
}
