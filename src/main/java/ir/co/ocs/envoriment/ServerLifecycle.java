package ir.co.ocs.envoriment;

public interface ServerLifecycle {
    NetworkChannel start();

    NetworkChannel stop();

    NetworkChannel restart();
}
