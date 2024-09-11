package ir.co.ocs.socketconfiguration;

import ir.co.ocs.Processor;
import ir.co.ocs.codec.FixedLengthByteArrayFactory;
import ir.co.ocs.envoriment.networkchannel.SSLManger;
import ir.co.ocs.messageprotocol.ProtocolMessageFactory;
import lombok.*;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;

import java.util.HashMap;

/**
 * Configuration class for TCP server socket settings.
 * <p>
 * This class extends {@link DefaultSocketSessionConfig} and is used to configure various aspects
 * of a TCP server socket, including the port, SSL settings, and protocol factories.
 * It utilizes Lombok annotations {@code @Getter} for generating getters and
 * {@code @Builder} for creating instances via the builder pattern.
 * </p>
 *
 * <p>
 * The following fields are available for configuration:
 * </p>
 *
 * <ul>
 *   <li>{@code channelIdentificationName}: A unique name used to identify the channel.</li>
 *   <li>{@code port}: The port number on which the server socket will listen.</li>
 *   <li>{@code ssl}: A boolean flag indicating whether SSL/TLS is enabled for the socket.</li>
 *   <li>{@code keyStorePath}: The file path to the keystore containing the server's private key and certificate.</li>
 *   <li>{@code trustStorePath}: The file path to the truststore containing the server's trusted certificates.</li>
 *   <li>{@code channelAttribute}: A map of attributes associated with the channel, used for custom configurations.</li>
 *   <li>{@code messageProtocolFactory}: A factory for creating message protocol instances.</li>
 *   <li>{@code protocolCodecFactory}: A factory for creating protocol codec instances, which handle the encoding and decoding of messages.</li>
 *   <li>{@code processor}: The processor responsible for handling the processing of incoming and outgoing messages.</li>
 * </ul>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>{@code
 * DefaultTcpSocketConfiguration config = DefaultTcpSocketConfiguration.builder()
 *     .channelIdentificationName("MyServerChannel")
 *     .port(8080)
 *     .ssl(true)
 *     .keyStorePath("/path/to/keystore")
 *     .trustStorePath("/path/to/truststore")
 *     .channelAttribute(new HashMap<>())
 *     .messageProtocolFactory(new MyMessageProtocolFactory())
 *     .protocolCodecFactory(new MyProtocolCodecFactory())
 *     .processor(new MyProcessor())
 *     .build();
 * }</pre>
 *
 * @see DefaultSocketSessionConfig
 */
@Getter
@Setter
public class BaseTcpSocketConfiguration extends DefaultSocketSessionConfig implements SocketConfiguration {
    private String channelIdentificationName;//mandatory
    private int port;//mandatory
    private SSLManger sslManger;
    private HashMap<Object, Object> channelAttribute;//optional if is not set default
    private ProtocolMessageFactory protocolMessageFactory;
    private ProtocolCodecFactory protocolCodecFactory;//optional if is not set default
    private Processor processor;//mandatory
    private boolean permanent;


    public BaseTcpSocketConfiguration() {
        setBothIdleTime(2);
        setReaderIdleTime(2);
        setWriterIdleTime(2);
        setKeepAlive(true);
        permanent = true;
        channelAttribute = new HashMap<>();
    }


    @Override
    public void validate() {
        if (channelIdentificationName == null || channelIdentificationName.isEmpty()) {
            throw new IllegalArgumentException("Channel Identification Name is mandatory.");
        }

        if (port <= 0) {
            throw new IllegalArgumentException("Port must be a positive integer.");
        }

        if (processor == null) {
            //throw new IllegalArgumentException("Processor is mandatory.");
            //todo check for processor
        }

        // Optionally ensure default protocol codec factory if not set
        if (protocolCodecFactory == null) {
            // Set a default value or throw an exception if you prefer strict validation
            this.protocolCodecFactory = new FixedLengthByteArrayFactory();
        }
    }
}
