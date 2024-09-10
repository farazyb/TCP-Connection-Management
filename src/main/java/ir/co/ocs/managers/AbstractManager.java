package ir.co.ocs.managers;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;
import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
public abstract class AbstractManager<T extends NetworkChannel> implements Manager<T>,Restartable {
    protected final Map<String, T> services = new HashMap<>();


    @Override
    public void add(T networkChannel) {
        String channelName = networkChannel.getConfiguration().getChannelIdentificationName();

        if (services.containsKey(channelName)) {
            throw new IllegalArgumentException("Service with name '" + channelName + "' already exists.");
        }

        services.put(channelName, networkChannel);
        startConnection(networkChannel);

    }

    @Override
    public T remove(String identificationName) {
        T networkChannel = services.get(identificationName);
        if (networkChannel != null) {
            services.remove(identificationName);
            log.info("Service removed: " + identificationName);
            return networkChannel;
        } else {
            throw new IllegalArgumentException("Service with name '" + identificationName + "' not registered");
        }
    }

    @Override
    public void stop(String identificationName) {
        NetworkChannel networkChannel = services.get(identificationName);
        if (networkChannel != null) {
            remove(identificationName);
            networkChannel.stop();
            log.info("Service " + identificationName + " stopped.");
        } else {
            throw new IllegalArgumentException("Service with ID " + identificationName + " not found.");
        }
    }

    public void startConnections() {
        for (T networkChannel : services.values()) {
            startConnection(networkChannel);
        }
    }

    public void stopConnections() {
        for (NetworkChannel networkChannel : services.values()) {
            networkChannel.stop();
        }
    }

    public void restartConnections() {
        stopConnections();
        startConnections();
    }

    @Override
    public NetworkChannel removeAndStop(String identificationName) {
        T networkChannel = services.get(identificationName);
        if (networkChannel != null) {
            networkChannel.stop();
            services.remove(identificationName);
            log.info("Service removed: " + identificationName);
            return networkChannel;
        } else {
            throw new IllegalArgumentException("Service with name '" + identificationName + "' not registered");
        }
    }

    public abstract void shutdown();

    protected abstract void startConnection(T networkChannel);


}
