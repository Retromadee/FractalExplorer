package Explorer;

import java.util.ArrayDeque;
import java.util.Deque;

public class FractalHistory {
    private Deque<FractalState> undoStack = new ArrayDeque<>();
    private Deque<FractalState> redoStack = new ArrayDeque<>();
    private FractalState currentState;
    private final FractalUpdater fractalUpdater;
    
    public FractalHistory(FractalUpdater fractalUpdater) {
        this.fractalUpdater = fractalUpdater;
    }
    
    public void saveState(String fractalType, int depth, String colorValue) {
        // Clear redo stack when new action is performed
        redoStack.clear();
        
        // Save current state to undo stack if it exists
        if (currentState != null) {
            undoStack.push(currentState);
        }
        
        // Create and set new current state
        currentState = new FractalState(fractalType, depth, colorValue);
    }
    
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    public void undo() {
        if (!canUndo()) return;
        
        // Save current state to redo stack
        redoStack.push(currentState);
        
        // Get previous state
        currentState = undoStack.pop();
        
        // Apply the state
        applyState(currentState);
    }
    
    public void redo() {
        if (!canRedo()) return;
        
        // Save current state to undo stack
        undoStack.push(currentState);
        
        // Get next state
        currentState = redoStack.pop();
        
        // Apply the state
        applyState(currentState);
    }
    
    private void applyState(FractalState state) {
        switch (state.getFractalType()) {
            case "Sierpinski Triangle":
                fractalUpdater.getTriangleInstance().setDepth(state.getDepth());
                fractalUpdater.getTriangleInstance().setColor(state.getColorValue());
                break;
            case "Koch Snowflake":
                fractalUpdater.getKochSnowflakeInstance().setDepth(state.getDepth());
                fractalUpdater.getKochSnowflakeInstance().setColor(state.getColorValue());
                break;
            case "Mandelbrot Set":
                fractalUpdater.getMandelbrotSetInstance().setMaxIterations(state.getDepth());
                fractalUpdater.getMandelbrotSetInstance().setColorScheme(state.getColorValue());
                break;
        }
        fractalUpdater.updateFractal();
    }
    
    public static class FractalState {
        private String fractalType;
        private int depth;
        private String colorValue; // Hex color or scheme name
        
        public FractalState(String fractalType, int depth, String colorValue) {
            this.fractalType = fractalType;
            this.depth = depth;
            this.colorValue = colorValue;
        }
        
        // Getters
        public String getFractalType() {
            return fractalType;
        }
        
        public int getDepth() {
            return depth;
        }
        
        public String getColorValue() {
            return colorValue;
        }
    }
}
