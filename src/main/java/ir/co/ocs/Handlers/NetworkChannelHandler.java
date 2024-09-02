package ir.co.ocs.Handlers;

import ir.co.ocs.ChannelInformation;
import lombok.extern.log4j.Log4j;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Log4j
public abstract class NetworkChannelHandler extends IoHandlerAdapter {


    private ChannelInformation channelInformation;
    private HashMap<Object, Object> channelAttribute;


    public void setChannelInformation(ChannelInformation channelInformation) {
        this.channelInformation = channelInformation;
    }

    public void setChannelAttribute(HashMap<Object, Object> channelAttribute) {
        this.channelAttribute = channelAttribute;
    }

    @Override
    public final void sessionCreated(IoSession session) throws Exception {
        log.info(" ECHO THREAD : {" + Thread.currentThread().getName() + "}");
        log.info("adding channel attribute");
        addChannelAttribute(session);
    }

    private void addChannelAttribute(IoSession session) {
        if (channelAttribute != null && !channelAttribute.isEmpty()) {
            channelAttribute.forEach(session::setAttribute);
        }
    }

    @Override
    public final void messageReceived(IoSession session, Object message) throws Exception {

        String str = new String((byte[])message);
        log.info("Recived Message : "+str);
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
        cause.printStackTrace();
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        log.info("Message : {" + message + "} wrote for Session : {" + session.getLocalAddress() + "}");
    }
}
