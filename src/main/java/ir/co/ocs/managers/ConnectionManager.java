package ir.co.ocs.managers;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;

public interface ConnectionManager<T extends NetworkChannel> {
    void manageConnection(T networkChannel) throws InterruptedException;
}
