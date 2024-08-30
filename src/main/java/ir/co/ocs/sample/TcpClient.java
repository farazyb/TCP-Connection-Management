package ir.co.ocs.sample;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class TcpClient {
    public static void main(String[] args) {
        String host = "localhost";  // Replace with the server's IP address or hostname
        int port = 8080;  // Replace with the server's port
        for (int i = 0; i < 3; i++) {


            new Thread(() -> {
                do {
                    try (Socket socket = new Socket(host, port)) {
                        //  TimeUnit.SECONDS.sleep(10);


                        OutputStream outputStream = socket.getOutputStream();
                        InputStream inputStream = socket.getInputStream();

                        // Example message
                        String message = "Hello, server!";
                        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

                        // Convert the length of the message to a 4-digit string
                        String lengthPrefix = String.format("%04d", messageBytes.length);
                        byte[] lengthBytes = lengthPrefix.getBytes(StandardCharsets.UTF_8);

                        // Combine length and message bytes
                        ByteBuffer byteBuffer = ByteBuffer.allocate(lengthBytes.length + messageBytes.length);
                        byteBuffer.put(lengthBytes);
                        byteBuffer.put(messageBytes);

                        // Send the combined byte array
                        outputStream.write(byteBuffer.array());
                        outputStream.flush();
                        System.out.println("Message sent to the server.");

                        // Read the length prefix from the server's response
                        byte[] responseLengthBytes = new byte[4];
                        int bytesRead = inputStream.read(responseLengthBytes);
                        if (bytesRead != 4) {
                            throw new IllegalStateException("Unexpected response length prefix size.");
                        }

                        // Parse the length of the response message
                        int responseLength = Integer.parseInt(new String(responseLengthBytes, StandardCharsets.UTF_8));

                        // Read the actual response message
                        byte[] responseBytes = new byte[responseLength];
                        bytesRead = inputStream.read(responseBytes);
                        if (bytesRead != responseLength) {
                            throw new IllegalStateException("Unexpected response message size.");
                        }

                        String responseMessage = new String(responseBytes, StandardCharsets.UTF_8);
                        System.out.println("Received response from the server: " + responseMessage);
                        TimeUnit.MILLISECONDS.sleep(500);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (true);
            }).start();
        }
    }
}

