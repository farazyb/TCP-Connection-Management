package ir.co.ocs.Handlers;

import ir.co.ocs.ChannelInformation;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void sessionCreated(IoSession session) throws Exception {
        logger.info(" ECHO THREAD : {}", Thread.currentThread().getName());
        session.setAttribute("Channel", this.channelInformation);
    }
}
