package Fractals;

import Panels.SnowflakePanel;
import java.awt.*;
import javax.swing.*;

public class KochSnowflake extends JPanel {
    private SnowflakePanel snowflakePanel;
    private JSpinner depthSpinner;

    //added constatnts to make it easier to edit later on
    private static final int INITIAL_DEPTH = 5;
    private static final int MIN_DEPTH = 1;
    private static final int MAX_DEPTH = 10;
    private static final int STEP = 1;

    public KochSnowflake(SwingWorker<JPanel, Void> fractalUpdater) {
        setLayout(new BorderLayout());  // Use BorderLayout for consistent layout

        

        snowflakePanel = new SnowflakePanel();
        depthSpinner = new JSpinner(new SpinnerNumberModel(INITIAL_DEPTH, MIN_DEPTH, MAX_DEPTH, STEP));

        //Added to update the panel to the desired depth as soon as it launches
        snowflakePanel.setDepth(INITIAL_DEPTH);

        depthSpinner.addChangeListener(e -> {
            // Pass the selected depth to the fractal panel
            snowflakePanel.setDepth((Integer) depthSpinner.getValue());
            snowflakePanel.repaint(); // Repaint the panel to update the fractal
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Recursion Depth:"));
        controlPanel.add(depthSpinner);

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
