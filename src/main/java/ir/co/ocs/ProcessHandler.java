package ir.co.ocs;

import ir.co.ocs.connections.DataInformation;

import java.util.concurrent.CompletableFuture;


public abstract class ProcessHandler {


    public abstract ProcessHandler getInstance();

    public abstract CompletableFuture<DataInformation> doProcess(DataInformation receivedDataInformation);

}
