package Fractals;

import Explorer.FractalUpdater;
import Panels.TrianglePanel;

import javax.swing.*;
import java.awt.*;

public class SierpinskiTriangle extends JPanel {
    private TrianglePanel fractalPanel;
    private JSpinner depthSpinner;

    public SierpinskiTriangle(FractalUpdater fractalUpdater) {
        // Set up the panel layout
        setLayout(new BorderLayout());

        // Create the fractal panel and the spinner for depth selection
        fractalPanel = new TrianglePanel();
        depthSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));

        // Update the fractal when the spinner value changes
        depthSpinner.addChangeListener(e -> {
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
