package ClientServer;

import Explorer.FractalExplorer;
import java.io.*;
import java.net.*;

public class FractalServer {
    private static ServerSocket serverSocket;
    private static FractalExplorer fractalExplorer;
    public static void startServer(FractalExplorer explorer) throws IOException {
        int port = 12345;
        
        // //added a way to edit the port (if needed) default is still 12345
        // if (args.length > 0) {
        //     try {
        //         port = Integer.parseInt(args[0]);
        //     } catch (NumberFormatException e) {
        //         System.out.println("Invalid port number, using default port 12345.");
        //     }
        // }
        fractalExplorer = explorer;
        serverSocket = new ServerSocket(port);
        System.out.println("Server is running on port "+ port +" ...");
        
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
            fractalExplorer.processReceivedMessage(input);

            out.println("Fractal parameters received.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void closeServer(){
        try{
            if(serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
                System.out.println("Server Stopped.");
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
