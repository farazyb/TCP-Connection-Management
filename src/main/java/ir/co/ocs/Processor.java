package ir.co.ocs;

import ir.co.ocs.connections.DataInformation;
import org.apache.mina.core.session.IoSession;

public interface Processor<T> extends Runnable {

    public DataInformation<T> read();

    public void write(DataInformation<T> dataInformation);
}
