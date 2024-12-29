package Explorer;

import ClientServer.FractalServer;
import Fractals.KochSnowflake;
import Fractals.MandelbrotSet;
import Fractals.SierpinskiTriangle;
import Presets.PresetPanel;
import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

public class FractalExplorer extends JFrame {
    private JPanel controlPanel,serverPanel,fractalContainer;
    private JComboBox<String> fractalComboBox;
    private FractalUpdater fractalUpdater;
    private JButton serverButton, stopServerButton, sendConfig;
    private JLabel serverLabel;
    private JDialog serverDialog;
    private Thread serverThread;
    private FractalHistory fractalHistory;
    private FractalHistory.FractalState fractalState;
    private JButton undoButton;
    private JButton redoButton;
    private JMenuBar menuBar = new JMenuBar();;
    private JMenu menu, presetMenu;
    private JMenuItem serverSettingsMenuItem, saveImagMenuItem, presetsMenuItem;
    


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
        fractalUpdater = new FractalUpdater(fractalContainer, fractalComboBox,fractalHistory);
        
        fractalHistory = new FractalHistory(fractalUpdater);
//
//// Create undo/redo buttons
//        undoButton = new JButton("Undo");
//        redoButton = new JButton("Redo");
//        undoButton.setEnabled(false);
//        redoButton.setEnabled(false);
//
//        undoButton.addActionListener(_ -> {
//            fractalHistory.undo();
//            updateUndoRedoButtons();
//        });
//
//        redoButton.addActionListener(_ -> {
//            fractalHistory.redo();
//            updateUndoRedoButtons();
//        });
//
//        controlPanel.add(undoButton);
//        controlPanel.add(redoButton);

        // ActionListener for JComboBox
        fractalComboBox.addActionListener(_ -> fractalUpdater.updateFractal());

        // Server Label
        serverLabel = new JLabel("server not started");

        serverDialog = new JDialog(this, "Server Settings", true);
        serverPanel = new JPanel();
    
        serverDialog.add(serverPanel);
        
        

        // Server Start Button
        serverButton = new JButton("Start Server");
        serverButton.addActionListener(_ -> {
            startServer();

        });
        
        //Server Stop Button
        stopServerButton = new JButton("Stop Server");
        stopServerButton.setVisible(false);
        stopServerButton.setEnabled(false);
        stopServerButton.addActionListener(_ ->{
            stopServer();

        }); 

        //Sending Configs Button
        sendConfig = new JButton("Send Config");
        sendConfig.addActionListener(_ -> fractalUpdater.sendParametersToClient());

        //Menu Bar
        presetMenu = new JMenu("Presets");
        menu = new JMenu("Options");
        menuBar.add(menu);
        menuBar.add(presetMenu);

        //Server Settings MenuItem
        serverSettingsMenuItem = new JMenuItem("Server Settings");
        serverSettingsMenuItem.addActionListener(_ -> {
            serverDialog.setLocationRelativeTo(this); 
            serverDialog.setVisible(true);
        });


        //Save as Image MenuItem
        saveImagMenuItem= new JMenuItem("Save As Image");
        saveImagMenuItem.addActionListener(_ -> fractalUpdater.saveFractal());

        //Presets MenuItem
        presetsMenuItem = new JMenuItem("Preset Settings");
        presetsMenuItem.addActionListener(_ -> showPresetPanel());

        serverPanel.add(serverButton);
        serverPanel.add(stopServerButton);
        serverPanel.add(serverLabel);
        serverPanel.add(sendConfig);
        serverDialog.pack();

        controlPanel.add(new JLabel("Select Fractal:"));
        controlPanel.add(fractalComboBox);

        // Add control panel and fractal container to the JFrame
        add(controlPanel, BorderLayout.NORTH);
        add(fractalContainer, BorderLayout.CENTER);

        presetMenu.add(presetsMenuItem);

        menu.add(serverSettingsMenuItem);
        menu.add(saveImagMenuItem);
        setJMenuBar(menuBar);
        
        fractalUpdater.updateFractal();
    }
     private void showPresetPanel() {
        PresetPanel presetFrame = fractalUpdater.getPresetPanel();
        presetFrame.setTitle("Preset Settings");
        presetFrame.setSize(400, 300); 
        presetFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        presetFrame.pack();
        presetFrame.setLocationRelativeTo(this);
        presetFrame.setVisible(true); 
    }
    
    public void updateUndoRedoButtons() {
        undoButton.setEnabled(fractalHistory.canUndo());
        redoButton.setEnabled(fractalHistory.canRedo());
    }
    
    
    private void startServer() {
        // Update the label that the server is starting
        SwingUtilities.invokeLater(() -> serverLabel.setText("Starting server."));
        SwingUtilities.invokeLater(() -> serverButton.setVisible(false));

        // Start the server in a background thread using a Thread
        serverThread = new Thread(() -> {
            try {
                // Update the label after the server has started
                SwingUtilities.invokeLater(() -> serverLabel.setText("Server is running."));
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

        String regex = "(Mandelbrot Set|Sierpinski Triangle|Koch Snowflake)\\s+depth:(\\d+)\\s+color:(#[0-9a-fA-F]{6}|Rainbow|Blue Orange|Cool Colors)";
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
                        fractalHistory.saveState(fractalType, Integer.parseInt(depth), color);
                        updateUndoRedoButtons();
                    });
                    break;
                
                
                case "Koch Snowflake":
                    SwingUtilities.invokeLater(()->{
                        kochSnowflakeInstance.setDepth(Integer.parseInt(depth));
                        fractalComboBox.setSelectedItem(fractalType);  
                        kochSnowflakeInstance.setColor(color);
                        fractalHistory.saveState(fractalType, Integer.parseInt(depth), color);
                        updateUndoRedoButtons();
                    });
                    break;
                case "Mandelbrot Set":
                    SwingUtilities.invokeLater(()->{
                        mandelBrotInstance.setMaxIterations(Integer.parseInt(depth));
                        fractalComboBox.setSelectedItem(fractalType);  
                        
                        mandelBrotInstance.setColorScheme(color);
                        fractalHistory.saveState(fractalType, Integer.parseInt(depth), color);
                        updateUndoRedoButtons();
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
