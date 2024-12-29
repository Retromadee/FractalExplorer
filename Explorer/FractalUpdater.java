package Explorer;

import ClientServer.FractalClient;
import Fractals.KochSnowflake;
import Fractals.MandelbrotSet;
import Fractals.SierpinskiTriangle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class FractalUpdater {
    private JPanel fractalContainer;
    private JComboBox<String> fractalComboBox;
    private SierpinskiTriangle sierpinskiTriangleInstance;
    private KochSnowflake kochSnowflakeInstance;
    private MandelbrotSet mandelbrotSetInstance;
    private BufferedImage image;
    private FractalHistory fractalHistory;
    private FractalHistory.FractalState fractalState;
    private JButton undoButton;
    private JButton redoButton;
    
    
    public FractalUpdater(JPanel fractalContainer, JComboBox<String> fractalComboBox, FractalHistory fractalHistory) {
        this.fractalContainer = fractalContainer;
        this.fractalComboBox = fractalComboBox;
        this.fractalHistory = fractalHistory;
        
    }

    public void updateFractal() {
        String selectedFractal = (String) fractalComboBox.getSelectedItem();
        
        // Remove any existing fractal panel to ensure a clean update
        fractalContainer.removeAll();
        
        // Start the background process with SwingWorker
        new SwingWorker<JPanel, Void>() {
            @Override
            protected JPanel doInBackground() throws Exception {
                JPanel fractalPanel = new JPanel(new BorderLayout());
                // Initialize the fractals if not already done
                if (sierpinskiTriangleInstance == null) {
                    sierpinskiTriangleInstance = new SierpinskiTriangle(this);
                }
                if (mandelbrotSetInstance == null) {
                    mandelbrotSetInstance = new MandelbrotSet(this);
                }
                if (kochSnowflakeInstance == null) {
                    kochSnowflakeInstance = new KochSnowflake(this);
                }
    
                // Switch case for selected fractal and add it to the panel
                switch (selectedFractal) {
                    case "Sierpinski Triangle":
                        fractalPanel.add(sierpinskiTriangleInstance, BorderLayout.CENTER);
                        break;
                    case "Mandelbrot Set":
                        fractalPanel.add(mandelbrotSetInstance, BorderLayout.CENTER);
                        break;
                    case "Koch Snowflake":
                        fractalPanel.add(kochSnowflakeInstance, BorderLayout.CENTER);
                        break;
                    default:
                        throw new AssertionError("Unknown fractal type");
                }
    
                return fractalPanel;
            }
    
            @Override
            protected void done() {
                try {
                    JPanel fractalPanel = get(); // Get the fractal panel created in doInBackground
                    // Ensure the new panel is added to the container and UI is updated
                    fractalContainer.add(fractalPanel, BorderLayout.CENTER);
                    fractalContainer.revalidate();
                    fractalContainer.repaint();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    public SierpinskiTriangle getTriangleInstance(){
        return sierpinskiTriangleInstance;
    }
    public KochSnowflake getKochSnowflakeInstance(){
        return kochSnowflakeInstance;
    }
    public MandelbrotSet getMandelbrotSetInstance(){
        return mandelbrotSetInstance;
    }
    
    public void saveFractal() {
        String selectedFractal = (String) fractalComboBox.getSelectedItem();
        File outputFile;
        switch (selectedFractal){
    
            case "Sierpinski Triangle":
                image = sierpinskiTriangleInstance.captureFractal();
                outputFile = new File("Sierpinski Triangle Fractal.png");
                break;
            case "Koch Snowflake":
                image = kochSnowflakeInstance.captureFractal();
                outputFile = new File("Koch Snowflake Fractal.png");
                break;
            case "Mandelbrot Set":
                image = mandelbrotSetInstance.captureFractal();
                outputFile = new File("MandelBrot Set Fractal.png");
                break;
            default:
                throw new AssertionError();}
        try {
            ImageIO.write(image, "PNG", outputFile);
            System.out.println("Fractal saved as image.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving fractal image.");
        }
    }
    
    public void sendParametersToClient() {
        String selectedFractal = (String) fractalComboBox.getSelectedItem();
        int depth;
        String colorString;
        
        switch (selectedFractal) {
            case "Sierpinski Triangle":
                depth = sierpinskiTriangleInstance.getDepth();
                //Color triangleColor = sierpinskiTriangleInstance.getColor();
                colorString = sierpinskiTriangleInstance.getColor();
                //colorString = String.format("#%02x%02x%02x", triangleColor.getRed(), triangleColor.getGreen(), triangleColor.getBlue());
                break;
            case "Koch Snowflake":
                depth = kochSnowflakeInstance.getDepth();
                colorString = kochSnowflakeInstance.getColor(); // Now correctly handling string return type
                break;
            case "Mandelbrot Set":
                depth = mandelbrotSetInstance.getIterations();
                colorString = mandelbrotSetInstance.getColorScheme();
                System.out.println(mandelbrotSetInstance.getColorScheme());
                break;
            default:
                throw new AssertionError();
        }
        String parameters = selectedFractal + " depth:" + depth + " color:" + colorString;
        FractalClient.sendFractalParameters(parameters);
        
        fractalHistory.saveState(selectedFractal, depth, colorString);
        updateUndoRedoButtons();
    }
    
    private void updateUndoRedoButtons() {
        undoButton.setEnabled(fractalHistory.canUndo());
        redoButton.setEnabled(fractalHistory.canRedo());
    }
//

}

