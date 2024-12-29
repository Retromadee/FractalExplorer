package Presets;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Map;

public class PresetPanel extends JPanel {
    private final PresetManager presetManager;
    private final JComboBox<FractalPreset> presetComboBox;
    private final String fractalType;
    private final PresetLoadCallback loadCallback;
    private final PresetSaveCallback saveCallback;
    
    public interface PresetLoadCallback {
        void onPresetLoad(Map<String, Serializable> settings);
    }
    
    public interface PresetSaveCallback {
        Map<String, Serializable> onPresetSave();
    }
    
    public PresetPanel(String fractalType, PresetLoadCallback loadCallback, PresetSaveCallback saveCallback) {
        this.fractalType = fractalType;
        this.loadCallback = loadCallback;
        this.saveCallback = saveCallback;
        this.presetManager = PresetManager.getInstance();
        
        setLayout(new FlowLayout());
        
        presetComboBox = new JComboBox<>();
        updatePresetList();
        
        JButton saveButton = new JButton("Save Preset");
        JButton loadButton = new JButton("Load Preset");
        JButton deleteButton = new JButton("Delete Preset");
        
        saveButton.addActionListener(e -> saveCurrentAsPreset());
        loadButton.addActionListener(e -> loadSelectedPreset());
        deleteButton.addActionListener(e -> deleteSelectedPreset());
        
        add(new JLabel("Presets:"));
        add(presetComboBox);
        add(saveButton);
        add(loadButton);
        add(deleteButton);
    }
    
    private void updatePresetList() {
        presetComboBox.removeAllItems();
        for (FractalPreset preset : presetManager.getPresetsForType(fractalType)) {
            presetComboBox.addItem(preset);
        }
    }
    
    private void saveCurrentAsPreset() {
        String name = JOptionPane.showInputDialog("Enter preset name:");
        if (name != null && !name.trim().isEmpty()) {
            Map<String, Serializable> settings = saveCallback.onPresetSave();
            presetManager.savePreset(name, fractalType, settings);
            updatePresetList();
        }
    }
    
    private void loadSelectedPreset() {
        FractalPreset preset = (FractalPreset) presetComboBox.getSelectedItem();
        if (preset != null) {
            loadCallback.onPresetLoad(preset.getSettings());
        }
    }
    
    private void deleteSelectedPreset() {
        FractalPreset preset = (FractalPreset) presetComboBox.getSelectedItem();
        if (preset != null) {
            presetManager.deletePreset(fractalType, preset);
            updatePresetList();
        }
    }
}