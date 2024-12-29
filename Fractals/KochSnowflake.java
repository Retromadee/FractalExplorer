package Fractals;

import Panels.SnowflakePanel;
import Panels.SnowflakePanel.ColorScheme;
import Presets.PresetPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class KochSnowflake extends JPanel {
    private SnowflakePanel snowflakePanel;
    private JSpinner depthSpinner;
    private JComboBox<String> colorSchemeBox;
    private JButton colorPickerButton;
    
    private static final int INITIAL_DEPTH = 3;
    private static final int MIN_DEPTH = 0;
    private static final int MAX_DEPTH = 10;
    private static final int STEP = 1;
    
    public KochSnowflake(SwingWorker<JPanel, Void> fractalUpdater) {
        setLayout(new BorderLayout());
        
        snowflakePanel = new SnowflakePanel();
        
        // Create controls
        depthSpinner = new JSpinner(new SpinnerNumberModel(INITIAL_DEPTH, MIN_DEPTH, MAX_DEPTH, STEP));
        colorSchemeBox = new JComboBox<>(new String[]{"Classic", "Rainbow", "Cool Colors"});
        colorPickerButton = new JButton("Background Color");
        
        // Set up event listeners
        depthSpinner.addChangeListener(_ -> {
            snowflakePanel.setDepth((Integer) depthSpinner.getValue());
        });
        
        colorSchemeBox.addActionListener(_ -> {
            String selectedScheme = (String) colorSchemeBox.getSelectedItem();
            snowflakePanel.setColorScheme(ColorScheme.valueOf(selectedScheme.toUpperCase().replace(" ", "_")));
        });
        
        colorPickerButton.addActionListener(_ -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Background Color", Color.WHITE);
            if (newColor != null) {
                snowflakePanel.setBackgroundColor(newColor);
            }
        });
        
        // Create control panel
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Depth:"));
        controlPanel.add(depthSpinner);
        controlPanel.add(new JLabel("Color Scheme:"));
        controlPanel.add(colorSchemeBox);
        controlPanel.add(colorPickerButton);
        
        // Add components to main panel
        add(controlPanel, BorderLayout.NORTH);
        add(snowflakePanel, BorderLayout.CENTER);
        
        // Initial setup
        snowflakePanel.setDepth(INITIAL_DEPTH);
    }
    public PresetPanel getPresetPanel() {
            return snowflakePanel.getPresetPanel();
        }
    
    public BufferedImage captureFractal() {
        return snowflakePanel.captureFractal();
    }
    
    public int getDepth() {
        return snowflakePanel.getDepth();
    }
    
    public void setDepth(int depth) {
        if (depth >= MIN_DEPTH && depth <= MAX_DEPTH) {
            depthSpinner.setValue(depth);
            snowflakePanel.setDepth(depth);
        }
    }
    
    public void setColor(String colorStr) {
        snowflakePanel.setColor(colorStr);
        if (!colorStr.startsWith("#")) {
            String displayScheme = switch (colorStr) {
                case "Rainbow" -> "Rainbow";
                case "Cool Colors" -> "Cool Colors";
                default -> "Classic";
            };
            colorSchemeBox.setSelectedItem(displayScheme);
        }
    }
    
    public String getColor() {
        return snowflakePanel.getColor();
    }
    
    public void setColorScheme(String scheme) {
        ColorScheme cScheme = ColorScheme.valueOf(scheme.toUpperCase().replace(" ", "_"));
        String displayScheme = switch (cScheme) {
            case CLASSIC -> "Classic";
            case RAINBOW -> "Rainbow";
            case COOL_COLORS -> "Cool Colors";
        };
        colorSchemeBox.setSelectedItem(displayScheme);
        snowflakePanel.setColorScheme(cScheme);
    }
}