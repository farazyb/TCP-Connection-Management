package ir.co.ocs.socketconfiguration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientSocketConfiguration extends BaseTcpSocketConfiguration {
    private String host;

    private boolean highPriority;
    private int maxRetries;
    private int retryInterval;

    @Override
    public void validate() {
        super.validate(); // Call base validations
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Host is mandatory for client configuration.");
        }
        // Additional client-specific validation
    }

}
