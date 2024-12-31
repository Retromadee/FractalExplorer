package Panels;

import Explorer.FractalUpdater;
import Presets.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class SnowflakePanel extends JPanel {
    public enum ColorScheme {
        CLASSIC, RAINBOW, COOL_COLORS
    }
    
    private int depth = 3;
    private Color color = Color.BLUE;
    private Color backgroundColor = Color.WHITE;
    private ColorScheme colorScheme = ColorScheme.CLASSIC;
    private Image fractalImage;
    private PresetPanel presetPanel;
        private FractalUpdater fractalUpdater;
    
    public SnowflakePanel(FractalUpdater fractalUpdater) {
        this.fractalUpdater = fractalUpdater;
        setLayout(new BorderLayout());
        
        PresetPanel.PresetLoadCallback loadCallback = settings -> {
            depth = (Integer) settings.get("depth");
            colorScheme = ColorScheme.valueOf(((String) settings.get("colorScheme")).toUpperCase().replace(" ", "_"));
            backgroundColor = new Color((Integer) settings.get("backgroundColor"));
            fractalUpdater.getKochSnowflakeInstance().updateGui();
            generateFractalImage();
        };
        
        PresetPanel.PresetSaveCallback saveCallback = () -> {
            Map<String, Serializable> settings = new HashMap<>();
            settings.put("depth", depth);
            settings.put("colorScheme", colorScheme.name());
            settings.put("backgroundColor", backgroundColor.getRGB());
            return settings;
        };
        
        presetPanel = new PresetPanel("Snowflake", loadCallback, saveCallback);
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
    
    public void setColorScheme(ColorScheme scheme) {
        this.colorScheme = scheme;
        generateFractalImage();
    }
    
    public ColorScheme getColorScheme() {
        return colorScheme;
    }
    
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        generateFractalImage();
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public void setColor(String colorStr) {
        if (colorStr.startsWith("#")) {
            this.color = Color.decode(colorStr);
            this.colorScheme = ColorScheme.CLASSIC;
        } else {
            switch (colorStr) {
                case "Rainbow" -> this.colorScheme = ColorScheme.RAINBOW;
                case "Cool colors" -> this.colorScheme = ColorScheme.COOL_COLORS;
                default -> {
                    this.color = Color.BLUE;
                    this.colorScheme = ColorScheme.CLASSIC;
                }
            }
        }
        generateFractalImage();
    }
    
    public String getColor() {
            return switch (colorScheme) {
                case RAINBOW -> "Rainbow";
                case COOL_COLORS -> "Cool colors";
                default -> "Classic"; // Default blue
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
                g2d.setColor(backgroundColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Enable anti-aliasing for smoother lines
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = Math.min(getWidth(), getHeight()) / 2;
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 3;
                
                drawSnowflake(g2d, centerX, centerY, size, depth);
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
    
    private void drawSnowflake(Graphics2D g, int x, int y, int size, int depth) {
        int halfSize = size / 2;
        int height = (int) (Math.sqrt(3) * halfSize);
        
        int x1 = x;
        int y1 = y;
        int x2 = x - halfSize;
        int y2 = y + height;
        int x3 = x + halfSize;
        int y3 = y + height;
        
        drawKochCurve(g, x1, y1, x2, y2, depth, 0);
        drawKochCurve(g, x2, y2, x3, y3, depth, 1);
        drawKochCurve(g, x3, y3, x1, y1, depth, 2);
    }
    
    private void drawKochCurve(Graphics2D g, int x1, int y1, int x2, int y2, int depth, int side) {
        if (depth == 0) {
            g.setColor(getColorForScheme(side, depth));
            g.drawLine(x1, y1, x2, y2);
        } else {
            double x3 = (2 * x1 + x2) / 3.0;
            double y3 = (2 * y1 + y2) / 3.0;
            double x4 = (x1 + 2 * x2) / 3.0;
            double y4 = (y1 + 2 * y2) / 3.0;
            
            double dx = x4 - x3;
            double dy = y4 - y3;
            double x5 = x3 + dx / 2 - dy * Math.sqrt(3) / 2;
            double y5 = y3 + dy / 2 + dx * Math.sqrt(3) / 2;
            
            drawKochCurve(g, x1, y1, (int) x3, (int) y3, depth - 1, side);
            drawKochCurve(g, (int) x3, (int) y3, (int) x5, (int) y5, depth - 1, side);
            drawKochCurve(g, (int) x5, (int) y5, (int) x4, (int) y4, depth - 1, side);
            drawKochCurve(g, (int) x4, (int) y4, x2, y2, depth - 1, side);
        }
    }
    
    private Color getColorForScheme(int side, int depth) {
        return switch (colorScheme) {
            case CLASSIC -> color;
            case RAINBOW -> getRainbowColor(depth * 30 + side * 120);
            case COOL_COLORS -> getCoolColor(depth * 20 + side * 90);
            default -> Color.BLACK;
        };
    }
    
    private Color getRainbowColor(int i) {
        int r = (int) (255 * Math.sin(0.016 * i));
        int g = (int) (255 * Math.sin(0.013 * i + 2));
        int b = (int) (255 * Math.sin(0.01 * i + 4));
        return new Color(
                Math.max(0, Math.min(255, r)),
                Math.max(0, Math.min(255, g)),
                Math.max(0, Math.min(255, b))
        );
    }
    
    private Color getCoolColor(int i) {
        int r = (int) (255 * Math.sin(0.01 * i));
        int g = (int) (255 * Math.cos(0.02 * i));
        int b = (int) (255 * Math.sin(0.03 * i));
        return new Color(
                Math.max(0, Math.min(255, r)),
                Math.max(0, Math.min(255, g)),
                Math.max(0, Math.min(255, b))
        );
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