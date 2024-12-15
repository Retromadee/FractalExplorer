package Fractals;

import javax.swing.*;
import java.awt.*;

public class SierpinskiTriangle extends JFrame {
    private TrianglePanel fractalPanel;
    private JSpinner depthSpinner;

    public SierpinskiTriangle() {
        setTitle("Sierpinski Triangle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        
        // Create GUI components
        fractalPanel = new TrianglePanel();
        depthSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        depthSpinner.addChangeListener(e -> updateFractal());

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Recursion Depth:"));
        controlPanel.add(depthSpinner);

        add(controlPanel, BorderLayout.NORTH);
        add(fractalPanel, BorderLayout.CENTER);

        updateFractal();
    }

    private void updateFractal() {
        int depth = (int) depthSpinner.getValue();
        fractalPanel.setDepth(depth);
        fractalPanel.repaint();
    }


}

