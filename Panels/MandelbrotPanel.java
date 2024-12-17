package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MandelbrotPanel extends JPanel {
    private double zoom = 200;
    private int maxIterations = 500;
    private double offsetX = -0.7, offsetY = 0.0;
    private Color backgroundColor ;
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
                double zx = 1.5 * (x - getWidth() / 2) / zoom + offsetX;
                double zy = (y - getHeight() / 2) / zoom + offsetY;
                int i = maxIterations;
                while (zx * zx + zy * zy < 4 && i > 0) {
                    double temp = zx * zx - zy * zy + offsetX;
                    zy = 2.0 * zx * zy + offsetY;
                    zx = temp;
                    i--;
                }

                // Color based on iterations
                int r = (int) ((i * 255.0) / maxIterations);
                int gColor = (int) ((i * 128.0) / maxIterations);
                int b = (int) ((i * 64.0) / maxIterations);
                g.setColor(new Color(r, gColor, b)); // fractal shows with this,
//                //but cant seem to change background color

//                int r = (int) ((i * 255.0) / maxIterations);
//                int gColor = (int) ((i * 128.0) / maxIterations);
//                int b = (int) ((i * 64.0) / maxIterations);
//                g.setColor(backgroundColor); // Changes the background color,
                // but fractal is not showing
                //in the process

                g.fillRect(x, y, 1, 1);
            }
        }
    }
}
