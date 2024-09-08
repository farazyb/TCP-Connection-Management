package ir.co.ocs.envoriment.server;

import ir.co.ocs.managers.AbstractManager;
import lombok.extern.log4j.Log4j;

@Log4j
public class ServerManager extends AbstractManager<Server> {
    @Override
    public void startConnection(Server server) {
        server.start();
        log.info("Server " + server.getIdentification() + " Starts Listening on port:" + server.getServerConfig().getPort());
    }

}
