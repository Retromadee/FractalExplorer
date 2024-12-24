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
        String selectedFractal = (String) fractalComboBox.getSelectedItem();
        
        // Remove any existing fractal panel
        fractalContainer.removeAll();
        
        // Creating the Mulithreading with SwingWorker ;
        // Apparently its more Swing friendly than Executor,
        // and runnable is just a no chance.
        
        new SwingWorker<JPanel, Void>(){
            @Override
            protected JPanel doInBackground() throws Exception {
                JPanel fractalPanel = new JPanel(new BorderLayout());
                // Adding back the fractals into the SwingWorker,
                // since we want to thread their processes
                if (sierpinskiTriangleInstance == null) {
                    sierpinskiTriangleInstance = new SierpinskiTriangle(this);
                }
                if (mandelbrotSetInstance == null) {
                    mandelbrotSetInstance = new MandelbrotSet(this);
                }
                if (kochSnowflakeInstance == null) {
                    kochSnowflakeInstance = new KochSnowflake(this);
                }
                switch (selectedFractal) {
                    
                    case "Sierpinski Triangle"->fractalPanel.add(sierpinskiTriangleInstance, BorderLayout.CENTER);
                
                    case "Mandelbrot Set" -> fractalPanel.add(mandelbrotSetInstance, BorderLayout.CENTER);

                    case "Koch Snowflake"->fractalPanel.add(kochSnowflakeInstance, BorderLayout.CENTER);

                }
                
                
//                Thread.sleep(1000);
                System.out.println("Selected Fractal: " + selectedFractal);

                return fractalPanel;
            }
            @Override
            protected void done(){
                try {
                    JPanel fractalPanel = get();
                    fractalContainer.add(fractalPanel,BorderLayout.CENTER);
                    // Revalidate and repaint the container to display the selected fractal
                    fractalContainer.revalidate();
                    fractalContainer.repaint();
                    
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
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

