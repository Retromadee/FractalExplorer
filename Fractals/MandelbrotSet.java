package Fractals;

import Explorer.FractalUpdater;
import Panels.MandelbrotPanel;
import Panels.MandelbrotPanel.ColorScheme;
import Presets.PresetPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class MandelbrotSet extends JPanel {
    private MandelbrotPanel mandelbrotPanel;
    private JComboBox<Integer> maxIterations;
    private JSlider zoomSlider;
    private JComboBox<String> mandelColorBox;

    public MandelbrotSet(FractalUpdater fractalUpdater) {
        setLayout(new BorderLayout());

        mandelbrotPanel = new MandelbrotPanel(fractalUpdater);
        maxIterations = new JComboBox<>(new Integer[]{25, 50, 100, 250, 500, 1000, 2500});

        maxIterations.setSelectedItem(1000);
        mandelbrotPanel.setMaxIterations((Integer) maxIterations.getSelectedItem());

        maxIterations.addActionListener(_ -> {
            // Pass the selected depth to the fractal panel
            mandelbrotPanel.setMaxIterations((Integer) maxIterations.getSelectedItem());
            // mandelbrotPanel.repaint(); // Repaint the panel to update the fractal
        });

        // Add Zoom Slider (JSlider for Zoom)
        zoomSlider = new JSlider(250, 1000, 250);  // Range from 50 to 1000 with initial zoom 200
        zoomSlider.addChangeListener(_ -> {
            mandelbrotPanel.setZoom(zoomSlider.getValue());
            // mandelbrotPanel.repaint();
        });


        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Iterations:"));
        controlPanel.add(maxIterations);
        
        controlPanel.add(new JLabel("Zoom:"));
        controlPanel.add(zoomSlider);

        mandelColorBox = new JComboBox<>();
        mandelColorBox.addItem("Rainbow");
        mandelColorBox.addItem("Blue Orange");
        mandelColorBox.addItem("Cool colors");
        
        mandelColorBox.addActionListener(_ ->{
            String selectedItem =(String) mandelColorBox.getSelectedItem();

            switch (selectedItem){
                case "Rainbow" -> mandelbrotPanel.setColorScheme(ColorScheme.RAINBOW);
                case "Blue Orange" -> mandelbrotPanel.setColorScheme(ColorScheme.BLUE_ORANGE);
                case "Cool colors" -> mandelbrotPanel.setColorScheme(ColorScheme.COOL_COLORS);
            }
            }   
        );

        controlPanel.add(mandelColorBox);
        add(controlPanel, BorderLayout.NORTH);
        add(mandelbrotPanel, BorderLayout.CENTER);

        }
        public void updateGui(){
            System.out.println(mandelbrotPanel.getColorScheme().toString());
            maxIterations.setSelectedItem((Integer) mandelbrotPanel.getMaxIterations());
            Double zoom = mandelbrotPanel.getZoom();
            zoomSlider.setValue((Integer) zoom.intValue());
            String colorScheme = mandelbrotPanel.getColorScheme().toString().replace("_", " ");
            colorScheme = colorScheme.substring(0, 1).toUpperCase() + colorScheme.substring(1).toLowerCase();
            mandelColorBox.setSelectedItem(colorScheme);
        }
        public PresetPanel getPresetPanel() {
            return mandelbrotPanel.getPresetPanel();
        }
        public int getIterations(){
            return (Integer) maxIterations.getSelectedItem();
        }
        public String getColorScheme(){
            return (String) mandelColorBox.getSelectedItem();
        }
        public void setMaxIterations(int iterations){
            mandelbrotPanel.setMaxIterations(iterations);
            maxIterations.setSelectedItem(iterations);
        }
        public void setColorScheme(String scheme) {
            ColorScheme cScheme = ColorScheme.valueOf(scheme.toUpperCase().replace(" ", "_"));
            mandelbrotPanel.setColorScheme(cScheme); 
        
            String displayScheme = switch (cScheme) {
                case RAINBOW -> "Rainbow";
                case BLUE_ORANGE -> "Blue Orange";
                case COOL_COLORS -> "Cool colors";
            };
            mandelColorBox.setSelectedItem(displayScheme); 
        }
        
        
        public BufferedImage captureFractal() {
            BufferedImage image = new BufferedImage(mandelbrotPanel.getWidth(), mandelbrotPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            mandelbrotPanel.paint(g2d);
            g2d.dispose();
            return image;
        }
}