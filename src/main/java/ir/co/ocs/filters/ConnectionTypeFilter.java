package ir.co.ocs.filters;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

public class ConnectionTypeFilter extends IoFilterAdapter {

    private boolean isPermanent = true;

    public ConnectionTypeFilter() {
    }

    public ConnectionTypeFilter(boolean isPermanent) {
        this.isPermanent = isPermanent;
    }

    @Override
    public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        if (!isPermanent) session.closeOnFlush();
        super.messageSent(nextFilter, session, writeRequest);
    }
}
