package Fractals;

import javax.swing.*;
import java.awt.*;

public class MandelbrotSet extends JFrame {
    private MandelbrotPanel mandelbrotPanel;
    private JSpinner depthSpinner;

    public MandelbrotSet() {
        setTitle("Mandelbrot Set");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        // Create GUI components
        mandelbrotPanel = new MandelbrotPanel();
        depthSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 1));
        depthSpinner.addChangeListener(e -> updateFractal());

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Max Iterations:"));
        controlPanel.add(depthSpinner);

        add(controlPanel, BorderLayout.NORTH);
        add(mandelbrotPanel, BorderLayout.CENTER);

        updateFractal();
    }

    private void updateFractal() {
        int maxIterations = (int) depthSpinner.getValue();
        mandelbrotPanel.setMaxIterations(maxIterations);
        mandelbrotPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MandelbrotSet frame = new MandelbrotSet();
            frame.setVisible(true);
        });
    }
}


