package Fractals;

import Panels.TrianglePanel;
import java.awt.*;
import javax.swing.*;

public class SierpinskiTriangle extends JPanel {
    private TrianglePanel trianglePanel;
    private JSpinner depthSpinner;
    private JButton colorPickerButton;
    private JColorChooser colorPicker;

    //added constatnts to make it easier to edit later on
    private static final int INITIAL_DEPTH = 3;
    private static final int MIN_DEPTH = 0;
    private static final int MAX_DEPTH = 7;
    private static final int STEP = 1;

    private Color selectedColor = Color.BLUE; 

    

    public SierpinskiTriangle(SwingWorker<JPanel, Void> fractalUpdater) {
        // Set up the panel layout
        setLayout(new BorderLayout());

        // Create the fractal panel and the spinner for depth selection
        trianglePanel = new TrianglePanel();

        colorPicker = new JColorChooser();
        colorPickerButton = new JButton("Pick A Color");
       

        colorPickerButton.addActionListener(_ -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Fractal Color", selectedColor);
            if (newColor != null) {
                selectedColor = newColor;
                trianglePanel.setColor(newColor);
                trianglePanel.repaint();
            }
        });
        // Set the depth to the initial depth so it automatically shows it when the app starts
        trianglePanel.setDepth(INITIAL_DEPTH);

        depthSpinner = new JSpinner(new SpinnerNumberModel(INITIAL_DEPTH, MIN_DEPTH, MAX_DEPTH, STEP));

        // Update the fractal when the spinner value changes
        depthSpinner.addChangeListener(_ -> {
            // Pass the selected depth to the fractal panel
            trianglePanel.setDepth((Integer) depthSpinner.getValue());
            trianglePanel.repaint(); // Repaint the panel to update the fractal
        });

        // Create the control panel for the spinner
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Recursion Depth:"));
        controlPanel.add(depthSpinner);
        controlPanel.add(colorPickerButton);

        // Add the control panel and fractal panel to the main panel
        add(controlPanel, BorderLayout.NORTH);
        add(trianglePanel, BorderLayout.CENTER);
        
        
        }
         // Method to return the current depth as a string
    public int getDepth() {  
        return (Integer) depthSpinner.getValue();
    }
    public void setDepth(int depth) {
        depthSpinner.setValue(depth);
        // fractalPanel.setDepth(depth); 
        trianglePanel.repaint(); 
    }
    public Color getColor(){
        return trianglePanel.getcolor();
    }
    public void setColor(String colorString){
        Color color = Color.decode(colorString);
        trianglePanel.setColor(color);
    }
    }

