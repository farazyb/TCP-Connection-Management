package ir.co.ocs.envoriment.networkchannel;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;

public interface ServiceLifecycle {
    NetworkChannel start() throws InterruptedException;

    NetworkChannel stop();

    NetworkChannel restart() throws InterruptedException;
}
