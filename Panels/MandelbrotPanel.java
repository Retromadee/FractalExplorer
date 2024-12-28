package Panels;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;


public class MandelbrotPanel extends JPanel {
    public enum ColorScheme {
        RAINBOW, BLUE_ORANGE, COOL_COLORS
    }

    private double zoom = 200;
    private int maxIterations = 250;
    private double offsetX = -0.7, offsetY = 0.0;
    private Color backgroundColor = Color.BLACK;
    private ColorScheme colorScheme = ColorScheme.RAINBOW;

    private Image fractalImage;

    // Set zoom level and max iterations
    public void setZoom(double zoom) {
        this.zoom = zoom;
        generateFractalImage();
    }

    public double getZoom() {
        return zoom;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        generateFractalImage();
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
        generateFractalImage();
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
        generateFractalImage(); 
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        generateFractalImage();
    }

    public void setColorScheme(ColorScheme scheme) {
        this.colorScheme = scheme;
        generateFractalImage();
    }

    public void generateFractalImage() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;  // it kept throwing errors when width and height were 0
        }
    
        new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() throws Exception {
                BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < getWidth(); x++) {
                    for (int y = 0; y < getHeight(); y++) {
                        double zx = 0, zy = 0;
                        double cx = 1.5 * (x - getWidth() / 2) / zoom + offsetX;
                        double cy = (y - getHeight() / 2) / zoom + offsetY;
                        int i = maxIterations;
    
                        // Mandelbrot iteration
                        while (zx * zx + zy * zy < 4 && i > 0) {
                            double temp = zx * zx - zy * zy + cx;
                            zy = 2.0 * zx * zy + cy;
                            zx = temp;
                            i--;
                        }
    
                        // Color selection based on color scheme
                        Color pixelColor = (i == maxIterations) ? Color.BLACK : getColorForScheme(i);
                        image.setRGB(x, y, pixelColor.getRGB());
                    }
                }
                return image;
            }
    
            @Override
            protected void done() {
                try {
                    fractalImage = get();
                    repaint(); // Repaint the panel once the image is generated
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    

    private Color getColorForScheme(int i) {
        return switch (colorScheme) {
            case RAINBOW -> getRainbowColor(i);
            case BLUE_ORANGE -> getBlueOrangeColor(i);
            case COOL_COLORS -> getCoolColor(i);
            default -> Color.BLACK;
        };
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
    int r = (int) (255 * (0.5 + 0.5 * Math.sin(0.3 * i)));
    int g = (int) (255 * (0.5 + 0.5 * Math.cos(0.2 * i)));
    int b = (int) (255 * (0.5 + 0.5 * Math.cos(0.1 * i)));

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fractalImage != null) {
            g.drawImage(fractalImage, 0, 0, this);
            
        }
        generateFractalImage();
    }
}
