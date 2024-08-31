package ir.co.ocs.envoriment;

import org.apache.mina.core.service.IoService;

public interface IoServiceFactory {
    IoService createIoService();
}
