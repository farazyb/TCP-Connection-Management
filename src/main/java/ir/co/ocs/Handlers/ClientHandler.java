package ir.co.ocs.Handlers;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link IoHandler} for SumUp client.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class ClientHandler extends NetworkChannelHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private final int[] values;

    private boolean finished;

    public ClientHandler(int[] values) {
        this.values = values;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void sessionOpened(IoSession session) {
        // send summation requests
        for (int i = 0; i < values.length; i++) {

            session.write((i + "").getBytes());
        }
    }


    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        System.out.println("Session Closed");
        System.out.println("session State:"+session.isActive());
    }
}
