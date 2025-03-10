package ir.co.ocs.api.service;

import ir.co.ocs.api.model.ServiceStatus;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ServiceManager {
    private final Map<String, ServiceStatus> services = new ConcurrentHashMap<>();
    private final Map<String, NioSocketAcceptor> acceptors = new ConcurrentHashMap<>();
    private final Map<String, NioSocketConnector> connectors = new ConcurrentHashMap<>();
    private final Map<String, Long> startTimes = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> activeConnections = new ConcurrentHashMap<>();

    public void registerServer(String serviceId, String host, int port, NioSocketAcceptor acceptor) {
        ServiceStatus status = ServiceStatus.builder()
                .serviceId(serviceId)
                .serviceType("SERVER")
                .status("STOPPED")
                .host(host)
                .port(port)
                .uptime(0)
                .activeConnections(0)
                .build();

        services.put(serviceId, status);
        acceptors.put(serviceId, acceptor);
        activeConnections.put(serviceId, new AtomicLong(0));

        addServiceListener(serviceId, acceptor);
    }

    public void registerClient(String serviceId, String host, int port, NioSocketConnector connector) {
        ServiceStatus status = ServiceStatus.builder()
                .serviceId(serviceId)
                .serviceType("CLIENT")
                .status("STOPPED")
                .host(host)
                .port(port)
                .uptime(0)
                .activeConnections(0)
                .build();

        services.put(serviceId, status);
        connectors.put(serviceId, connector);
        activeConnections.put(serviceId, new AtomicLong(0));

        addServiceListener(serviceId, connector);
    }

    private void addServiceListener(String serviceId, IoService service) {
        service.addListener(new IoServiceListener() {
            @Override
            public void serviceActivated(IoService service) {
                startTimes.put(serviceId, System.currentTimeMillis());
                updateStatus(serviceId, "RUNNING");
            }

            @Override
            public void serviceIdle(IoService service, IdleStatus idleStatus) {
                // Handle idle status if needed
            }

            @Override
            public void serviceDeactivated(IoService service) {
                updateStatus(serviceId, "STOPPED");
                startTimes.remove(serviceId);
            }

            @Override
            public void sessionCreated(IoSession session) {
                activeConnections.get(serviceId).incrementAndGet();
                updateServiceStatus(serviceId);
            }

            @Override
            public void sessionClosed(IoSession session) {
                activeConnections.get(serviceId).decrementAndGet();
                updateServiceStatus(serviceId);
            }

            @Override
            public void sessionDestroyed(IoSession session) throws Exception {

            }


        });
    }

    public void startService(String serviceId) {
        ServiceStatus status = services.get(serviceId);
        if (status != null) {
            if ("SERVER".equals(status.getServiceType())) {
                NioSocketAcceptor acceptor = acceptors.get(serviceId);
                if (acceptor != null && !acceptor.isActive()) {
                    try {
                        acceptor.bind(new InetSocketAddress(status.getHost(), status.getPort()));
                    } catch (Exception e) {
                        updateStatus(serviceId, "ERROR");
                        status.setLastError(e.getMessage());
                    }
                }
            } else if ("CLIENT".equals(status.getServiceType())) {
                NioSocketConnector connector = connectors.get(serviceId);
                if (connector != null && !connector.isActive()) {
                    try {
                        connector.connect(new InetSocketAddress(status.getHost(), status.getPort()));
                    } catch (Exception e) {
                        updateStatus(serviceId, "ERROR");
                        status.setLastError(e.getMessage());
                    }
                }
            }
        }
    }

    public void stopService(String serviceId) {
        ServiceStatus status = services.get(serviceId);
        if (status != null) {
            if ("SERVER".equals(status.getServiceType())) {
                NioSocketAcceptor acceptor = acceptors.get(serviceId);
                if (acceptor != null && acceptor.isActive()) {
                    acceptor.unbind();
                }
            } else if ("CLIENT".equals(status.getServiceType())) {
                NioSocketConnector connector = connectors.get(serviceId);
                if (connector != null && connector.isActive()) {
                    connector.dispose();
                }
            }
        }
    }

    public void restartService(String serviceId) {
        stopService(serviceId);
        startService(serviceId);
    }

    public ServiceStatus getServiceStatus(String serviceId) {
        updateServiceStatus(serviceId);
        return services.get(serviceId);
    }

    public Map<String, ServiceStatus> getAllServices() {
        services.keySet().forEach(this::updateServiceStatus);
        return services;
    }

    private void updateStatus(String serviceId, String status) {
        ServiceStatus serviceStatus = services.get(serviceId);
        if (serviceStatus != null) {
            serviceStatus.setStatus(status);
        }
    }

    private void updateServiceStatus(String serviceId) {
        ServiceStatus status = services.get(serviceId);
        if (status != null) {
            Long startTime = startTimes.get(serviceId);
            if (startTime != null) {
                status.setUptime(System.currentTimeMillis() - startTime);
            }
            status.setActiveConnections((int) activeConnections.get(serviceId).get());
        }
    }
} 