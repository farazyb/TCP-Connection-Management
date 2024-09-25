package ir.co.ocs.Handlers;

import ir.co.ocs.connections.DataInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends NetworkChannelHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    public ClientHandler() {

    }
    @Override
    public void readMessage(DataInformation dataInformation) {

    }
    public void write(DataInformation dataInformation){

    }

}
