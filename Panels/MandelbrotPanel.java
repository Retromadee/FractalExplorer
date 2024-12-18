package Panels;

import java.awt.*;
import javax.swing.*;
// Enum to define different color schemes


public class MandelbrotPanel extends JPanel {
    public  enum ColorScheme {
        RAINBOW,
        BLUE_ORANGE,
        COOL_COLORS
    }
    private double zoom = 200;
    private int maxIterations = 250;
    private double offsetX = -0.7, offsetY = 0.0;
    private Color backgroundColor ;
    ColorScheme colorScheme = ColorScheme.RAINBOW;
    //    = Color.BLACK;

    // Set zoom level and max iterations
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getZoom() {
        return zoom;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }
    public void setColorScheme(ColorScheme scheme){
        this.colorScheme = scheme;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(backgroundColor);
        g.setColor(Color.blue);
        drawMandelbrotSet(g);
    }
    
    
    // Draw Mandelbrot Set
    private void drawMandelbrotSet(Graphics g) {
    for (int x = 0; x < getWidth(); x++) {
        for (int y = 0; y < getHeight(); y++) {
            // Map pixel coordinates to complex plane (c = offsetX + offsetY*i)
            double zx = 0; // Initial z value (z0 = 0)
            double zy = 0; // Initial z value (z0 = 0)
            double cx = 1.5 * (x - getWidth() / 2) / zoom + offsetX; // Real part of c
            double cy = (y - getHeight() / 2) / zoom + offsetY; // Imaginary part of c
            int i = maxIterations;
            
            // Iterate the formula zₙ₊₁ = zₙ² + c until it escapes (|z| > 2) or reaches max iterations
            while (zx * zx + zy * zy < 4 && i > 0) {
                double temp = zx * zx - zy * zy + cx; // Real part of z = zₙ² + c (zx)
                zy = 2.0 * zx * zy + cy; // Imaginary part of z = 2 * zx * zy (zy)
                zx = temp; // Update zx
                i--; // Decrease iteration count
            }

                    // Color selection based on currentColorScheme
            Color pixelColor;
            if (i == maxIterations) {
                pixelColor = Color.BLACK;  // Points inside the Mandelbrot set are black
            } else {
                switch (colorScheme) {
                    case RAINBOW -> pixelColor = getRainbowColor(i);
                    case BLUE_ORANGE -> pixelColor = getBlueOrangeColor(i);
                    case COOL_COLORS -> pixelColor = getCoolColor(i);
                    default->pixelColor = Color.BLACK;
                }
            }
            g.setColor(pixelColor);
            g.fillRect(x, y, 1, 1);
        }
    }
}
// Rainbow color scheme function
private Color getRainbowColor(int i) {
    int r = (int) (255 * Math.sin(0.16 * i));
    int g = (int) (255 * Math.sin(0.13 * i));
    int b = (int) (255 * Math.sin(0.1 * i));

    r = Math.max(0, Math.min(255, r));
    g = Math.max(0, Math.min(255, g));
    b = Math.max(0, Math.min(255, b));
    return new Color(r, g, b);
}

// Blue-Orange color scheme function
private Color getBlueOrangeColor(int i) {
    int r = (int) (255 * (0.5 + 0.5 * Math.sin(0.3 * i)));  // Blue to orange transition
    int g = (int) (255 * (0.5 + 0.5 * Math.cos(0.2 * i)));  // Green in the middle
    int b = (int) (255 * (0.5 + 0.5 * Math.cos(0.1 * i)));  // Blue to orange transition

    r = Math.max(0, Math.min(255, r));
    g = Math.max(0, Math.min(255, g));
    b = Math.max(0, Math.min(255, b));
    return new Color(r, g, b);
}

// Cool color scheme function
private Color getCoolColor(int i) {
    int r = (int) (255 * Math.sin(0.1 * i));
    int g = (int) (255 * Math.cos(0.2 * i));
    int b = (int) (255 * Math.sin(0.3 * i));

    r = Math.max(0, Math.min(255, r));
    g = Math.max(0, Math.min(255, g));
    b = Math.max(0, Math.min(255, b));
    return new Color(r, g, b);
}
}
