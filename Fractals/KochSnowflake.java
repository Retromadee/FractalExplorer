package Fractals;

import Explorer.FractalUpdater;
import Panels.SnowflakePanel;

import javax.swing.*;
import java.awt.*;

public class KochSnowflake extends JPanel {
    private SnowflakePanel snowflakePanel;
    private JSpinner depthSpinner;

    public KochSnowflake(FractalUpdater fractalUpdater) {
        setLayout(new BorderLayout());  // Use BorderLayout for consistent layout

        snowflakePanel = new SnowflakePanel();
        depthSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));

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
}
