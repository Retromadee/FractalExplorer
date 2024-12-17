package Explorer;

import Fractals.KochSnowflake;
import Fractals.MandelbrotSet;
import Fractals.SierpinskiTriangle;

import javax.swing.*;
import java.awt.*;

public class FractalUpdater {
    private JPanel fractalContainer;
    private JComboBox<String> fractalComboBox;

    public FractalUpdater(JPanel fractalContainer, JComboBox<String> fractalComboBox) {
        this.fractalContainer = fractalContainer;
        this.fractalComboBox = fractalComboBox;
    }

    public void updateFractal() {
        String selectedFractal = (String) fractalComboBox.getSelectedItem();

        // Remove any existing fractal panel
        fractalContainer.removeAll();

        // Add the selected fractal panel
        switch (selectedFractal) {
            case "Sierpinski Triangle":
                fractalContainer.add(new SierpinskiTriangle(this), BorderLayout.CENTER);
                break;
            case "Mandelbrot Set":
                fractalContainer.add(new MandelbrotSet(this), BorderLayout.CENTER);
                break;
            case "Koch Snowflake":
                fractalContainer.add(new KochSnowflake(this), BorderLayout.CENTER);
                break;
        }

        // Revalidate and repaint the container to display the selected fractal
        fractalContainer.revalidate();
        fractalContainer.repaint();
    }
}
