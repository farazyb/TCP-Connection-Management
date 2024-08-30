package ir.co.ocs.messageprotocol;

public interface ProtocolMessageFactory {

    public MessageEncoder getEncoder();

    public MessageDecoder getDecoder();
}
