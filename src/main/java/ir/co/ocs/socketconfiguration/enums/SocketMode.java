package ir.co.ocs.socketconfiguration.enums;


/**
 * The `SocketMode` enum represents different modes of socket communication.
 */
public enum SocketMode {
    /**
     * Represents a listener mode for socket communication.
     */
    LISTENER,
    /**
     * Represents a sender mode for socket communication.
     */
    SENDER,
    /**
     * Represents both listener and sender modes for socket communication.
     */
    BOTH;

    /**
     * Retrieves the enum value based on a case-insensitive string representation.
     *
     * @param str The string representation of the enum value.
     * @return The corresponding enum value.
     * @throws IllegalArgumentException If the provided string does not match any enum value.
     */
    public static SocketMode getEnumFrom(String str) throws IllegalArgumentException {
        try {
            return SocketMode.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid value for MyEnum: " + str);
        }
    }
}

