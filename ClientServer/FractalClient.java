package ClientServer;

import java.io.*;
import java.net.*;

public class FractalClient {
    public static void sendFractalParameters(String parameters) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(parameters);
            System.out.println("Server response: " + in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sendFractalParameters("Type: Sierpinski, Depth: 5");
    }
}
