package ir.co.ocs.managers;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;
import ir.co.ocs.socketconfiguration.DefaultTcpSocketConfiguration;
import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Log4j
public abstract class AbstractManager implements Manager {
    private final Map<String, NetworkChannel> services = new HashMap<>();
    private final ExecutorService executorService;

    public AbstractManager() {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void add(NetworkChannel networkChannel) {
        services.put(networkChannel.getConfiguration().getChannelIdentificationName(), networkChannel);
        startClientConnection(networkChannel);

    }

    @Override
    public NetworkChannel remove(String identificationName) {
        NetworkChannel networkChannel = services.get(identificationName);
        if (networkChannel != null) {
            networkChannel.stop();
            services.remove(identificationName);
            log.info("Service removed: " + identificationName);
            return networkChannel;
        } else {
            log.error("Service with ID " + identificationName + " not found.");
            return null;
        }
    }

    @Override
    public void stop(String identificationName) {
        NetworkChannel client = services.get(identificationName);
        if (client != null) {
            client.stop();
            log.info("Service " + identificationName + " stopped.");
        } else {
            log.error("Service with ID " + identificationName + " not found.");
        }
    }

    public void startConnections() {
        for (NetworkChannel networkChannel : services.values()) {
            startClientConnection(networkChannel);
        }
    }

    public void stopConnections() {
        for (NetworkChannel networkChannel : services.values()) {
            networkChannel.stop();
        }
    }

    public void restart(String identificationName) {
        stop(identificationName);
        NetworkChannel networkChannel = remove(identificationName);
        add(networkChannel);

    }

    public void restartConnections() {
        stopConnections();
        startConnections();
    }


    private void startClientConnection(NetworkChannel networkChannel) {
        executorService.submit(() -> {
            try {
                manageConnection(networkChannel);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    protected abstract void manageConnection(NetworkChannel networkChannel) throws InterruptedException;


}
