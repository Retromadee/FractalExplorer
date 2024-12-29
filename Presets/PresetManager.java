package Presets;

import java.io.*;
import java.util.*;

public class PresetManager {
    private static PresetManager instance;
    private Map<String, List<FractalPreset>> presetsByType;
    private static final String PRESET_FILE = "fractal_presets.dat";
    
    private PresetManager() {
        presetsByType = new HashMap<>();
        loadPresets();
    }
    
    public static PresetManager getInstance() {
        if (instance == null) {
            instance = new PresetManager();
        }
        return instance;
    }
    
    public void savePreset(String name, String fractalType, Map<String, Serializable> settings) {
        FractalPreset preset = new FractalPreset(name, fractalType, settings);
        presetsByType.computeIfAbsent(fractalType, k -> new ArrayList<>()).add(preset);
        savePresetsToFile();
    }
    
    public List<FractalPreset> getPresetsForType(String fractalType) {
        return presetsByType.getOrDefault(fractalType, new ArrayList<>());
    }
    
    public void deletePreset(String fractalType, FractalPreset preset) {
        List<FractalPreset> presets = presetsByType.get(fractalType);
        if (presets != null) {
            presets.remove(preset);
            savePresetsToFile();
        }
    }
    
    private void savePresetsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRESET_FILE))) {
            oos.writeObject(presetsByType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadPresets() {
        File file = new File(PRESET_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                presetsByType = (Map<String, List<FractalPreset>>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                presetsByType = new HashMap<>();
            }
        }
    }
}