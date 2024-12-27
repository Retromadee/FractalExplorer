package Explorer;

import ClientServer.FractalServer;
import Fractals.KochSnowflake;
import Fractals.MandelbrotSet;
import Fractals.SierpinskiTriangle;
import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

public class FractalExplorer extends JFrame {
    private JPanel controlPanel;
    private JComboBox<String> fractalComboBox;
    private JPanel fractalContainer;
    private FractalUpdater fractalUpdater;
    private JButton serverButton;
    private JButton stopServerButton;
    private JButton sendConfig;
    private JLabel serverLabel;
    private Thread serverThread;


    public FractalExplorer() {

   
        
        setTitle("Fractal Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Control Panel with JComboBox for selecting fractal
        controlPanel = new JPanel(new FlowLayout());

        fractalComboBox = new JComboBox<>();
        fractalComboBox.addItem("Sierpinski Triangle");
        fractalComboBox.addItem("Mandelbrot Set");
        fractalComboBox.addItem("Koch Snowflake");

        // Create FractalUpdater object and pass the correct components
        CardLayout cardLayout = new CardLayout();
        fractalContainer = new JPanel(cardLayout);
        fractalUpdater = new FractalUpdater(fractalContainer, fractalComboBox);

        // ActionListener for JComboBox
        fractalComboBox.addActionListener(_ -> fractalUpdater.updateFractal());

        // Server Label
        serverLabel = new JLabel("server not started");

        // Server Button
        serverButton = new JButton("Start Server");
        serverButton.addActionListener(e -> startServer());

        //Server Stop Button
        stopServerButton = new JButton("Stop Server");
        stopServerButton.setVisible(false);
        stopServerButton.setEnabled(false);
        stopServerButton.addActionListener(_ -> stopServer()); 

        //Sending Configs Button
        sendConfig = new JButton("Send Config");
        sendConfig.addActionListener(_ -> sendParametersToClient());

        controlPanel.add(new JLabel("Select Fractal:"));
        controlPanel.add(fractalComboBox);
        controlPanel.add(serverButton);
        controlPanel.add(stopServerButton);
        controlPanel.add(serverLabel);
        controlPanel.add(sendConfig);
        

        // Add control panel and fractal container to the JFrame
        add(controlPanel, BorderLayout.NORTH);
        add(fractalContainer, BorderLayout.CENTER);
        
        // Initially display a fractal
        fractalUpdater.updateFractal();
    }
    
    private void sendParametersToClient(){
        String selectedFractal = (String) fractalComboBox.getSelectedItem();
        // System.out.println(selectedFractal);
        fractalUpdater.sendParametersToClient();

    }

    private void startServer() {
        // Update the label that the server is starting
        SwingUtilities.invokeLater(() -> serverLabel.setText("Starting server..."));
        SwingUtilities.invokeLater(() -> serverButton.setVisible(false));

        // Start the server in a background thread using a Thread
        serverThread = new Thread(() -> {
            try {
                // Update the label after the server has started
                SwingUtilities.invokeLater(() -> serverLabel.setText("Server is running on port 12345..."));
                SwingUtilities.invokeLater(()-> serverButton.setVisible(false));
                SwingUtilities.invokeLater(()-> {
                    stopServerButton.setEnabled(true);
                    stopServerButton.setVisible(true);
                });
                // Run the server
                FractalServer.startServer(this);
            } catch (IOException e) {
                // Catch any errors here
                SwingUtilities.invokeLater(() -> serverLabel.setText("Server failed to start."));
                SwingUtilities.invokeLater(() -> {
                    serverButton.setVisible(true); 
                    serverButton.setEnabled(true);
                    serverButton.setText("Retry Starting"); 
                });
                SwingUtilities.invokeLater(()-> {
                    stopServerButton.setEnabled(false);
                    stopServerButton.setVisible(false);
                });
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    //Stop the server by closing the socket with closeServer()
    private void stopServer(){
        if(serverThread!=null && serverThread.isAlive()){
            FractalServer.closeServer();
            SwingUtilities.invokeLater(()-> {
                serverLabel.setText("Server Stopped");
                stopServerButton.setEnabled(false);
                stopServerButton.setVisible(false);
            });
            SwingUtilities.invokeLater(()->{
                serverButton.setVisible(true);
                serverButton.setText("Start Server");
            });
        }
    }

    //process the message sent by the client instance
    public void processReceivedMessage(String message){
        SierpinskiTriangle triangleInstance = fractalUpdater.getTriangleInstance();
        MandelbrotSet mandelBrotInstance = fractalUpdater.getMandelbrotSetInstance();
        KochSnowflake kochSnowflakeInstance = fractalUpdater.getKochSnowflakeInstance();

        String regex = "(Mandelbrot Set|Sierpinski Triangle|Koch Snowflake)\\s+depth:(\\d+)\\s+color:(#[0-9a-fA-F]{6})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        if(matcher.matches()){
            String fractalType = matcher.group(1);
            String depth = matcher.group(2);
            String color = matcher.group(3);

            switch (fractalType) {
                case "Sierpinski Triangle":
                    SwingUtilities.invokeLater(()->{
                        triangleInstance.setDepth(Integer.parseInt(depth));
                        fractalComboBox.setSelectedItem(fractalType);

                        triangleInstance.setColor(color);
                    });
                    break;
                case "Koch Snowflake":
                    SwingUtilities.invokeLater(()->{
                        kochSnowflakeInstance.setDepth(Integer.parseInt(depth));
                        fractalComboBox.setSelectedItem(fractalType);  
                        kochSnowflakeInstance.setColor(color); 
                    });
                    break;
                case "Mandelbrot Set":
                    SwingUtilities.invokeLater(()->{
                        mandelBrotInstance.setMaxIterations(Integer.parseInt(depth));
                        fractalComboBox.setSelectedItem(fractalType);  
                    });
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FractalExplorer app = new FractalExplorer();
            app.setVisible(true);
        });
    }
}
