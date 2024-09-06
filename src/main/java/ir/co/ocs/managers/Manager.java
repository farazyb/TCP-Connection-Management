package ir.co.ocs.managers;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;

public interface Manager {
    public void add(NetworkChannel networkChannel);
    public void remove(String  identificationName);

    public void stop(String identificationName);
}
