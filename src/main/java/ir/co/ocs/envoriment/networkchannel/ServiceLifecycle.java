package ir.co.ocs.envoriment.networkchannel;

public interface ServiceLifecycle {
    NetworkChannel start() throws InterruptedException;

    void stop();

    NetworkChannel restart() ;
}
