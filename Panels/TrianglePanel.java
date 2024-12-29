package Panels;

import Presets.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class TrianglePanel extends JPanel {
    private int depth = 3;
    private Color customColor = Color.BLUE;
    private Color backgroundColor = Color.WHITE;
    private String colorScheme = "Classic";
    private Image fractalImage;
    private PresetPanel presetPanel;
    
    public TrianglePanel() {
        setLayout(new BorderLayout());
        
        PresetPanel.PresetLoadCallback loadCallback = settings -> {
            depth = (Integer) settings.get("depth");
            colorScheme = (String) settings.get("colorScheme");
            backgroundColor = new Color((Integer) settings.get("backgroundColor"));
            if (settings.containsKey("customColor")) {
                customColor = new Color((Integer) settings.get("customColor"));
            }
            generateFractalImage();
        };
        
        PresetPanel.PresetSaveCallback saveCallback = () -> {
            Map<String, Serializable> settings = new HashMap<>();
            settings.put("depth", depth);
            settings.put("colorScheme", colorScheme);
            settings.put("backgroundColor", backgroundColor.getRGB());
            if (customColor != null) {
                settings.put("customColor", customColor.getRGB());
            }
            return settings;
        };
        
        presetPanel = new PresetPanel("Triangle", loadCallback, saveCallback);
    }
    public PresetPanel getPresetPanel() {
        return presetPanel;  
    }
    
    public void setDepth(int depth) {
        this.depth = depth;
        generateFractalImage();
    }
    
    public int getDepth() {
        return depth;
    }
    
    public void setColorScheme(String scheme) {
        this.colorScheme = scheme;
        this.customColor = null;  // Reset custom color when using a scheme
        generateFractalImage();
    }
    
    public String getColorScheme() {
        return colorScheme;
    }
    
    public void setCustomColor(Color color) {
        this.customColor = color;
        this.colorScheme = "Custom";
        generateFractalImage();
    }
    
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        generateFractalImage();
    }
    
    private Color getCurrentColor(int depth) {
        if (customColor != null && colorScheme.equals("Custom")) {
            return customColor;
        }
        
        return switch (colorScheme) {
            case "Rainbow" -> {
                float hue = (float) depth / 7f;  // Vary hue based on depth
                yield Color.getHSBColor(hue, 0.8f, 0.9f);
            }
            case "Cool Colors" -> {
                float hue = 0.5f + (float) depth / 14f;  // Blues to purples
                yield Color.getHSBColor(hue, 0.7f, 0.9f);
            }
            default -> Color.BLUE;  // Classic scheme
        };
    }
    
    public void generateFractalImage() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        
        new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() {
                BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = image.createGraphics();
                
                // Set background
                g2d.setColor(backgroundColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Enable anti-aliasing
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Calculate size and position
                int size = Math.min(getWidth(), getHeight()) * 2 / 3;
                drawTriangle(g2d, getWidth() / 2, getHeight() / 4, size, depth);
                
                g2d.dispose();
                return image;
            }
            
            @Override
            protected void done() {
                try {
                    fractalImage = get();
                    repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    private void drawTriangle(Graphics2D g, int x, int y, int size, int depth) {
        g.setColor(getCurrentColor(depth));
        
        if (depth == 0) {
            int[] xPoints = {x, x + size / 2, x - size / 2};
            int[] yPoints = {y, y + size, y + size};
            g.fillPolygon(xPoints, yPoints, 3);
        } else {
            drawTriangle(g, x, y, size / 2, depth - 1);
            drawTriangle(g, x - size / 4, y + size / 2, size / 2, depth - 1);
            drawTriangle(g, x + size / 4, y + size / 2, size / 2, depth - 1);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fractalImage != null) {
            g.drawImage(fractalImage, 0, 0, this);
        } else {
            generateFractalImage();
        }
    }
    
    public BufferedImage captureFractal() {
        if (fractalImage != null) {
            BufferedImage capture = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = capture.createGraphics();
            g2d.drawImage(fractalImage, 0, 0, null);
            g2d.dispose();
            return capture;
        }
        return null;
    }
}