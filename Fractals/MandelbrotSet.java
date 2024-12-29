package Fractals;

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

    // private JButton colorPicker;
    // private Color backgroundColor = Color.BLACK;


    public MandelbrotSet(SwingWorker<JPanel, Void> fractalUpdater) {
        setLayout(new BorderLayout());

        mandelbrotPanel = new MandelbrotPanel();
        maxIterations = new JComboBox<>(new Integer[]{25, 50, 100, 250, 500, 1000, 2500});

        maxIterations.setSelectedItem(1000);
        mandelbrotPanel.setMaxIterations((Integer) maxIterations.getSelectedItem());

        maxIterations.addActionListener(_ -> {
            // Pass the selected depth to the fractal panel
            mandelbrotPanel.setMaxIterations((Integer) maxIterations.getSelectedItem());
            // mandelbrotPanel.repaint(); // Repaint the panel to update the fractal
        });

        // Add Zoom Slider (JSlider for Zoom)
        zoomSlider = new JSlider(50, 1000, 200);  // Range from 50 to 1000 with initial zoom 200
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
        mandelColorBox.addItem("Cool Colors");
        
        mandelColorBox.addActionListener(_ ->{
            String selectedItem =(String) mandelColorBox.getSelectedItem();

            switch (selectedItem){
                case "Rainbow" -> mandelbrotPanel.setColorScheme(ColorScheme.RAINBOW);
                case "Blue Orange" -> mandelbrotPanel.setColorScheme(ColorScheme.BLUE_ORANGE);
                case "Cool Colors" -> mandelbrotPanel.setColorScheme(ColorScheme.COOL_COLORS);
            }
                // mandelbrotPanel.repaint();
            }   
        );
        // colorPicker = new JButton("Pick Color");
        // colorPicker.addActionListener(e -> {
        //     JColorChooser colorChooser = new JColorChooser();
        //     Color color = JColorChooser.showDialog(this, "Choose Background Color", Color.black);
        //     if (color != null) {
        //         backgroundColor = color;
        //         mandelbrotPanel.setBackgroundColor(backgroundColor);
        //         mandelbrotPanel.repaint();
        //     }
        // });

        // controlPanel.add(colorPicker);
        // mandelbrotPanel.repaint();
        controlPanel.add(mandelColorBox);
        add(controlPanel, BorderLayout.NORTH);
        add(mandelbrotPanel, BorderLayout.CENTER);

        // Enable mouse click to zoom into the clicked part
        //        mandelbrotPanel.addMouseListener(new MouseAdapter() {
        //            @Override
        //            public void mousePressed(MouseEvent e) {
        //                int mouseX = e.getX();
        //                int mouseY = e.getY();
        //
        //                // Convert mouse coordinates to Mandelbrot coordinates
        //                double clickX = (mouseX - mandelbrotPanel.getWidth() / 2) /
        //                        mandelbrotPanel.getZoom() + mandelbrotPanel.getOffsetX();
        //                double clickY = (mouseY - mandelbrotPanel.getHeight() / 2) /
        //                        mandelbrotPanel.getZoom() + mandelbrotPanel.getOffsetY();
        //
        //                // Update offsets based on click location
        //                mandelbrotPanel.setOffsetX(clickX);
        //                mandelbrotPanel.setOffsetY(clickY);
        //
        //                // Repaint after updating the center of the zoom
        //                mandelbrotPanel.repaint();
        //            }
        //        });
        }
        public void updateGui(){
            maxIterations.setSelectedItem((Integer) mandelbrotPanel.getMaxIterations());
            Double zoom = mandelbrotPanel.getZoom();
            zoomSlider.setValue((Integer) zoom.intValue());
            mandelColorBox.setSelectedItem((ColorScheme) mandelbrotPanel.getColorScheme());
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
                case COOL_COLORS -> "Cool Colors";
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