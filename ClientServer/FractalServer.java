package ClientServer;

import java.io.*;
import java.net.*;

public class FractalServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server is running on port 12345...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String input = in.readLine();
            System.out.println("Received: " + input);

            out.println("Fractal parameters received.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
