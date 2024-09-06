package ir.co.ocs.envoriment.ioservicefactory;

import org.apache.mina.core.service.IoService;

public interface IoServiceFactory {
    IoService createServer();
}
