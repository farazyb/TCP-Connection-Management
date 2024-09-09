package ir.co.ocs.managers;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;

public interface Manager<T extends NetworkChannel> {
    
    public void add(T networkChannel);

    public NetworkChannel remove(String identificationName);

    public void stop(String identificationName);

    public NetworkChannel removeAndStop(String identification);
}
