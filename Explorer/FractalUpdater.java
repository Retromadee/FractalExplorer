package Explorer;

import ClientServer.FractalClient;
import Fractals.KochSnowflake;
import Fractals.MandelbrotSet;
import Fractals.SierpinskiTriangle;
import Presets.PresetPanel;
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
    private JButton undoButton;
    private JButton redoButton;
    private PresetPanel presetPanel;
    
    
    public FractalUpdater(JPanel fractalContainer, JComboBox<String> fractalComboBox) {
        this.fractalContainer = fractalContainer;
        this.fractalComboBox = fractalComboBox;
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
                    sierpinskiTriangleInstance = new SierpinskiTriangle(FractalUpdater.this);
                }
                if (mandelbrotSetInstance == null) {
                    mandelbrotSetInstance = new MandelbrotSet(FractalUpdater.this);
                }
                if (kochSnowflakeInstance == null) {
                    kochSnowflakeInstance = new KochSnowflake(FractalUpdater.this);
                }
    
                // Switch case for selected fractal and add it to the panel
                switch (selectedFractal) {
                    case "Sierpinski Triangle":
                        fractalPanel.add(sierpinskiTriangleInstance, BorderLayout.CENTER);
                        presetPanel = sierpinskiTriangleInstance.getPresetPanel();
                        break;
                    case "Mandelbrot Set":
                        fractalPanel.add(mandelbrotSetInstance, BorderLayout.CENTER);
                        presetPanel = mandelbrotSetInstance.getPresetPanel();
                        break;
                    case "Koch Snowflake":
                        fractalPanel.add(kochSnowflakeInstance, BorderLayout.CENTER);
                        presetPanel = kochSnowflakeInstance.getPresetPanel();
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
    public PresetPanel getPresetPanel() {
        return presetPanel;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendParametersToClient() {
        String selectedFractal = (String) fractalComboBox.getSelectedItem();
        int depth;
        String colorString;
        String parameters;
    
        switch (selectedFractal) {
            case "Sierpinski Triangle":
                depth = sierpinskiTriangleInstance.getDepth();
                colorString = sierpinskiTriangleInstance.getColor();
                int sierpinskiBackgroundColor = sierpinskiTriangleInstance.getBackgroundColor().getRGB();
                parameters = selectedFractal + " depth:" + depth + " color:" + colorString + " background:" + String.format("#%06X", (0xFFFFFF & sierpinskiBackgroundColor));
                break;
            case "Koch Snowflake":
                depth = kochSnowflakeInstance.getDepth();
                colorString = kochSnowflakeInstance.getColor();
                int kochBackgroundColor = kochSnowflakeInstance.getBackgroundColor().getRGB();
                parameters = selectedFractal + " depth:" + depth + " color:" + colorString + " background:" + String.format("#%06X", (0xFFFFFF & kochBackgroundColor));
                break;
            case "Mandelbrot Set":
                depth = mandelbrotSetInstance.getIterations();
                colorString = mandelbrotSetInstance.getColorScheme();
                parameters = selectedFractal + " depth:" + depth + " color:" + colorString;
                break;
            default:
                throw new AssertionError();
        }
        FractalClient.sendFractalParameters(parameters);
    }


}

