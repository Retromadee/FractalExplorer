package Presets;

import java.io.Serializable;
import java.util.Map;

public class FractalPreset implements Serializable {
    private String name;
    private String fractalType;
    private Map<String, Serializable> settings;
    
    public FractalPreset(String name, String fractalType, Map<String, Serializable> settings) {
        this.name = name;
        this.fractalType = fractalType;
        this.settings = settings;
    }
    
    public String getName() {
        return name;
    }
    
    public String getFractalType() {
        return fractalType;
    }
    
    public Map<String, Serializable> getSettings() {
        return settings;
    }
    
    @Override
    public String toString() {
        return name;
    }
}