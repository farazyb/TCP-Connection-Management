package ir.co.ocs.Handlers;

import ir.co.ocs.ChannelInformation;
import lombok.extern.log4j.Log4j;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Log4j
public abstract class NetworkChannelHandler extends IoHandlerAdapter {



    @Override
    public final void sessionCreated(IoSession session) throws Exception {
        log.info(" ECHO THREAD : {" + Thread.currentThread().getName() + "}");
        log.info("adding channel attribute");
    }

    @Override
    public final void messageReceived(IoSession session, Object message) throws Exception {
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
        cause.printStackTrace();
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    }
}
