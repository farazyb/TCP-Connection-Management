package ir.co.ocs.socketconfiguration;

import ir.co.ocs.Processor;
import ir.co.ocs.messageprotocol.ProtocolMessageFactory;
import ir.co.ocs.socketconfiguration.enums.SocketMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
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
public class DefaultTcpSocketConfiguration extends DefaultSocketSessionConfig {
    private String channelIdentificationName;
    private int port;
    private boolean ssl;
    private String keyStorePath;
    private String trustStorePath;
    private String keyStorePassword;
    private String trustStorePassword;
    private HashMap<Object, Object> channelAttribute;
    private ProtocolMessageFactory protocolMessageFactory;
    private ProtocolCodecFactory protocolCodecFactory;
    private Processor processor;
    private boolean permanent;
    private boolean highPriority;
    private SocketMode socketMode;

    public DefaultTcpSocketConfiguration() {
        setBothIdleTime(2);
        setReaderIdleTime(2);
        setWriterIdleTime(2);
        setKeepAlive(true);
        this.socketMode = SocketMode.BOTH;
        this.permanent = true;
        this.ssl = false;
        channelAttribute=new HashMap<>();
    }

    public String getChannelIdentificationName() {
        return channelIdentificationName;
    }

    public void setChannelIdentificationName(String channelIdentificationName) {
        this.channelIdentificationName = channelIdentificationName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public HashMap<Object, Object> getChannelAttribute() {
        return channelAttribute;
    }

    public void setChannelAttribute(HashMap<Object, Object> channelAttribute) {
        this.channelAttribute = channelAttribute;
    }

    public ProtocolMessageFactory getProtocolMessageFactory() {
        return protocolMessageFactory;
    }

    public void setProtocolMessageFactory(ProtocolMessageFactory protocolMessageFactory) {
        this.protocolMessageFactory = protocolMessageFactory;
    }

    public ProtocolCodecFactory getProtocolCodecFactory() {
        return protocolCodecFactory;
    }

    public void setProtocolCodecFactory(ProtocolCodecFactory protocolCodecFactory) {
        this.protocolCodecFactory = protocolCodecFactory;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }

    public SocketMode getSocketMode() {
        return socketMode;
    }

    public void setSocketMode(SocketMode socketMode) {
        this.socketMode = socketMode;
    }
}
