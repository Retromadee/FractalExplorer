package Explorer;

import ClientServer.FractalClient;
import Fractals.KochSnowflake;
import Fractals.MandelbrotSet;
import Fractals.SierpinskiTriangle;
import java.awt.*;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public class FractalUpdater {
    private JPanel fractalContainer;
    private JComboBox<String> fractalComboBox;
    private SierpinskiTriangle sierpinskiTriangleInstance;
    private KochSnowflake kochSnowflakeInstance;
    private MandelbrotSet mandelbrotSetInstance;

    public FractalUpdater(JPanel fractalContainer, JComboBox<String> fractalComboBox) {
        this.fractalContainer = fractalContainer;
        this.fractalComboBox = fractalComboBox;
        
    }

    public void updateFractal() {
        System.out.println("updateFractal() is being called");
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
    public void sendParametersToClient(){
        String selectedFractal = (String) fractalComboBox.getSelectedItem();
        int depth = -1;
    
        
        switch (selectedFractal) {
            case "Sierpinski Triangle":
                depth = sierpinskiTriangleInstance.getDepth();
                
                break;
            case "Koch Snowflake":
                depth = kochSnowflakeInstance.getDepth();
                break;
            case "Mandelbrot Set":
                depth = mandelbrotSetInstance.getIterations();
                break;
            default:
                throw new AssertionError();
        }
        String parameters = selectedFractal + " depth:" + depth;
        FractalClient.sendFractalParameters(parameters);
    }
    
}

