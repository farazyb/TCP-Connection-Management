package ir.co.ocs.managers;

import ir.co.ocs.envoriment.networkchannel.NetworkChannel;
import ir.co.ocs.envoriment.server.Server;

public class ServerManager extends AbstractManager {
    @Override
    protected void manageConnection(NetworkChannel networkChannel) throws InterruptedException {
        if (networkChannel instanceof Server server) {
            server.start();
        } else {

        }
    }
}
