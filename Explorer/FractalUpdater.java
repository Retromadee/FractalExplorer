package Explorer;

import Fractals.KochSnowflake;
import Fractals.MandelbrotSet;
import Fractals.SierpinskiTriangle;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class FractalUpdater {
    private JPanel fractalContainer;
    private JComboBox<String> fractalComboBox;

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
                switch (selectedFractal) {
                    case "Sierpinski Triangle":
                        fractalPanel.add(new SierpinskiTriangle(this), BorderLayout.CENTER);
                        break;
                    case "Mandelbrot Set":
                        fractalPanel.add(new MandelbrotSet(this), BorderLayout.CENTER);
                        break;
                    case "Koch Snowflake":
                        fractalPanel.add(new KochSnowflake(this), BorderLayout.CENTER);
                        break;
                }
                
//                Thread.sleep(1000);
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
}
