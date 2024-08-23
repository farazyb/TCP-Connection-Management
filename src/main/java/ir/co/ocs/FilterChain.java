package ir.co.ocs;

import org.apache.mina.core.filterchain.IoFilter;

public abstract class FilterChain implements IoFilter {
    private String networkChannelIdentificationName;
     public void setName(String name){
        this.networkChannelIdentificationName=networkChannelIdentificationName;
    }
}
