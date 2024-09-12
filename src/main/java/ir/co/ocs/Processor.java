package ir.co.ocs;

import ir.co.ocs.connections.DataInformation;

public interface Processor extends Runnable {

     DataInformation read();

     void write(DataInformation dataInformation);
}
