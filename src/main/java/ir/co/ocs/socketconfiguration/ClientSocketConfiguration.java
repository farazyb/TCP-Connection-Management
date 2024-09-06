package ir.co.ocs.socketconfiguration;

public class ClientSocketConfiguration extends DefaultTcpSocketConfiguration {
    private String host;
    private boolean permanent;
    private boolean highPriority;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }
}
