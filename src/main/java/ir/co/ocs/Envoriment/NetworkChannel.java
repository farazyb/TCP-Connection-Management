package ir.co.ocs.Envoriment;

public interface NetworkChannel {

    public NetworkChannel start(int port);

    public NetworkChannel stop();

    public NetworkChannel restart();
}
