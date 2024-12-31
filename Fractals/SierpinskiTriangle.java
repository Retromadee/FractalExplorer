package Fractals;

import Explorer.FractalUpdater;
import Panels.TrianglePanel;
import Presets.PresetPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class SierpinskiTriangle extends JPanel {
    private TrianglePanel trianglePanel;
    private JSpinner depthSpinner;
    private JComboBox<String> colorSchemeBox;
    private JButton backgroundColorButton;
    
    private static final int INITIAL_DEPTH = 3;
    private static final int MIN_DEPTH = 0;
    private static final int MAX_DEPTH = 7;
    private static final int STEP = 1;
    
    public SierpinskiTriangle(FractalUpdater fractalUpdater) {
        setLayout(new BorderLayout());
        
        // Create panels and controls
        trianglePanel = new TrianglePanel(fractalUpdater);
        depthSpinner = new JSpinner(new SpinnerNumberModel(INITIAL_DEPTH, MIN_DEPTH, MAX_DEPTH, STEP));
        colorSchemeBox = new JComboBox<>(new String[]{"Classic", "Rainbow", "Cool colors"});
        backgroundColorButton = new JButton("Background Color");
        
        // Set up event listeners
        depthSpinner.addChangeListener(_ -> {
            trianglePanel.setDepth((Integer) depthSpinner.getValue());
            trianglePanel.repaint();
        });
        
        colorSchemeBox.addActionListener(_ -> {
            String selectedScheme = (String) colorSchemeBox.getSelectedItem();
            trianglePanel.setColorScheme(selectedScheme);
            trianglePanel.repaint();
        });
        
        backgroundColorButton.addActionListener(_ -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Background Color", Color.WHITE);
            if (newColor != null) {
                trianglePanel.setBackgroundColor(newColor);
                trianglePanel.repaint();
            }
        });
        
        // Create control panel
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Recursion Depth:"));
        controlPanel.add(depthSpinner);
        controlPanel.add(new JLabel("Color Scheme:"));
        controlPanel.add(colorSchemeBox);
        controlPanel.add(backgroundColorButton);
        
        // Add components to main panel
        add(controlPanel, BorderLayout.NORTH);
        add(trianglePanel, BorderLayout.CENTER);
        
        // Initial setup
        trianglePanel.setDepth(INITIAL_DEPTH);
        trianglePanel.setColorScheme("Classic");
    }
    public void updateGui() {
        depthSpinner.setValue(trianglePanel.getDepth());
        String colorScheme = trianglePanel.getColorScheme().replace("_", " ");
        colorScheme = colorScheme.substring(0, 1).toUpperCase() + colorScheme.substring(1).toLowerCase();
        colorSchemeBox.setSelectedItem(colorScheme);
    }
    public PresetPanel getPresetPanel() {
            return trianglePanel.getPresetPanel();
        }
    
    public BufferedImage captureFractal() {
        BufferedImage image = new BufferedImage(trianglePanel.getWidth(), trianglePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        trianglePanel.paint(g2d);
        g2d.dispose();
        return image;
    }
    
    public int getDepth() {
        return trianglePanel.getDepth();
    }
    
    public void setDepth(int depth) {
        if (depth >= MIN_DEPTH && depth <= MAX_DEPTH) {
            depthSpinner.setValue(depth);
            trianglePanel.setDepth(depth);
            trianglePanel.repaint();
        }
    }
    
    public String getColor() {
        return trianglePanel.getColorScheme();
    }
    public Color getBackgroundColor() {
        return trianglePanel.getBackgroundColor();
    }
    public void setBackgroundColor(Color color) {
        trianglePanel.setBackgroundColor(color);
        repaint();
    }
    
    public void setColor(String colorStr) {
        if (colorStr.startsWith("#")) {
            trianglePanel.setCustomColor(Color.decode(colorStr));
        } else {
            String scheme = switch (colorStr) {
                case "Rainbow" -> "Rainbow";
                case "Cool colors" -> "Cool colors";
                default -> "Classic";
            };
            colorSchemeBox.setSelectedItem(scheme);
            trianglePanel.setColorScheme(scheme);
        }
        trianglePanel.repaint();
    }
}