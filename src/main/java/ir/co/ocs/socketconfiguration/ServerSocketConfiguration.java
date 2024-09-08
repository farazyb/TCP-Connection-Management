package ir.co.ocs.socketconfiguration;

import ir.co.ocs.socketconfiguration.enums.SocketMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerSocketConfiguration extends BaseTcpSocketConfiguration {
    private SocketMode socketMode;

    @Override
    public void validate() {
        super.validate(); // Call base validations
        if (socketMode == null) {
            throw new IllegalArgumentException("Socket mode is mandatory for server configuration.");
        }
        // Additional server-specific validation
    }

}
