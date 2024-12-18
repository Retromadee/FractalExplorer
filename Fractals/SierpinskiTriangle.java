package Fractals;

import Panels.TrianglePanel;
import java.awt.*;
import javax.swing.*;

public class SierpinskiTriangle extends JPanel {
    private TrianglePanel fractalPanel;
    private JSpinner depthSpinner;

    //added constatnts to make it easier to edit later on
    private static final int INITIAL_DEPTH = 5;
    private static final int MIN_DEPTH = 1;
    private static final int MAX_DEPTH = 10;
    private static final int STEP = 1;

    public SierpinskiTriangle(SwingWorker<JPanel, Void> fractalUpdater) {
        // Set up the panel layout
        setLayout(new BorderLayout());

        // Create the fractal panel and the spinner for depth selection
        fractalPanel = new TrianglePanel();
        depthSpinner = new JSpinner(new SpinnerNumberModel(INITIAL_DEPTH, MIN_DEPTH, MAX_DEPTH, STEP));

        // Set the depth to the initial depth so it automatically shows it when the app starts
        fractalPanel.setDepth(INITIAL_DEPTH);

        // Update the fractal when the spinner value changes
        depthSpinner.addChangeListener(_ -> {
            // Pass the selected depth to the fractal panel
            fractalPanel.setDepth((Integer) depthSpinner.getValue());
            fractalPanel.repaint(); // Repaint the panel to update the fractal
        });

        // Create the control panel for the spinner
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Recursion Depth:"));
        controlPanel.add(depthSpinner);

        // Add the control panel and fractal panel to the main panel
        add(controlPanel, BorderLayout.NORTH);
        add(fractalPanel, BorderLayout.CENTER);
    }
}
