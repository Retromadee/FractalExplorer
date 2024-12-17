package Explorer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FractalExplorer extends JFrame {
    private JPanel controlPanel;
    private JComboBox<String> fractalComboBox;
    private JPanel fractalContainer;
    private FractalUpdater fractalUpdater;

    public FractalExplorer() {
        setTitle("Fractal Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Control Panel with JComboBox for selecting fractal
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        fractalComboBox = new JComboBox<>();
        fractalComboBox.addItem("Sierpinski Triangle");
        fractalComboBox.addItem("Mandelbrot Set");
        fractalComboBox.addItem("Koch Snowflake");

        // Create FractalUpdater object and pass the correct components
        fractalContainer = new JPanel();
        fractalContainer.setLayout(new BorderLayout());
        fractalUpdater = new FractalUpdater(fractalContainer, fractalComboBox);

        // ActionListener for JComboBox
        fractalComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractalUpdater.updateFractal();
            }
        });

        controlPanel.add(new JLabel("Select Fractal:"));
        controlPanel.add(fractalComboBox);

        // Add control panel and fractal container to the JFrame
        add(controlPanel, BorderLayout.NORTH);
        add(fractalContainer, BorderLayout.CENTER);

        // Initially display a fractal
        fractalUpdater.updateFractal();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FractalExplorer app = new FractalExplorer();
            app.setVisible(true);
        });
    }
}
