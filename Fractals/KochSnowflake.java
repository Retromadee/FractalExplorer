package Fractals;

import Panels.SnowflakePanel;
import java.awt.*;
import javax.swing.*;

public class KochSnowflake extends JPanel {
    private SnowflakePanel snowflakePanel;
    private JSpinner depthSpinner;
    private JButton colorPickerButton;
    private JColorChooser colorPicker;

    //added constatnts to make it easier to edit later on
    private static final int INITIAL_DEPTH = 3;
    private static final int MIN_DEPTH = 0;
    private static final int MAX_DEPTH = 10;
    private static final int STEP = 1;

    private Color selectedColor = Color.BLACK; 

    public KochSnowflake(SwingWorker<JPanel, Void> fractalUpdater) {
        setLayout(new BorderLayout());  // Use BorderLayout for consistent layout

        

        snowflakePanel = new SnowflakePanel();
        depthSpinner = new JSpinner(new SpinnerNumberModel(INITIAL_DEPTH, MIN_DEPTH, MAX_DEPTH, STEP));

        colorPicker = new JColorChooser();
        colorPickerButton = new JButton("Pick A Color");
       

        colorPickerButton.addActionListener(_ -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Fractal Color", selectedColor);
            if (newColor != null) {
                selectedColor = newColor;
                snowflakePanel.setColor(selectedColor);
                snowflakePanel.repaint();
            }
        });
        //Added to update the panel to the desired depth as soon as it launches
        snowflakePanel.setDepth(INITIAL_DEPTH);

        depthSpinner.addChangeListener(_ -> {
            // Pass the selected depth to the fractal panel
            snowflakePanel.setDepth((Integer) depthSpinner.getValue());
            snowflakePanel.repaint(); // Repaint the panel to update the fractal
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Recursion Depth:"));
        controlPanel.add(depthSpinner);
        controlPanel.add(colorPickerButton);

        add(controlPanel, BorderLayout.NORTH);
        add(snowflakePanel, BorderLayout.CENTER);
        

        // fractalUpdater.updateFractal();
    }
    
    public int getDepth() {  
        return (Integer) depthSpinner.getValue();
    }
    public void setDepth(int depth) {
        depthSpinner.setValue(depth);
        snowflakePanel.repaint(); 
    }
}
