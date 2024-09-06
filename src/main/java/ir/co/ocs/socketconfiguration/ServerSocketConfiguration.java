package ir.co.ocs.socketconfiguration;

import ir.co.ocs.socketconfiguration.enums.SocketMode;

public class ServerSocketConfiguration extends DefaultTcpSocketConfiguration{
    private SocketMode socketMode;

    public SocketMode getSocketMode() {
        return socketMode;
    }

    public void setSocketMode(SocketMode socketMode) {
        this.socketMode = socketMode;
    }
}
