package ir.co.ocs.connections;

import org.apache.mina.core.session.IoSession;

public class DataInformation {
    long sessionId;
    byte[] message;

    public DataInformation(long sessionId, byte[] message) {
        this.sessionId = sessionId;
        this.message = message;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
