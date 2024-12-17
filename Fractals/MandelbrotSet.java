package Fractals;

import Explorer.FractalUpdater;
import Panels.MandelbrotPanel;
import Panels.MandelbrotPanel.ColorScheme;
import java.awt.*;
import javax.swing.*;

public class MandelbrotSet extends JPanel {
    private MandelbrotPanel mandelbrotPanel;
    private JSpinner maxIterations;
    private JSlider zoomSlider;
    private JComboBox<String> mandelColorBox;
    private JButton colorPicker;
    private Color backgroundColor = Color.BLACK;

    public MandelbrotSet(FractalUpdater fractalUpdater) {
        setLayout(new BorderLayout());  // Use BorderLayout for consistent layout

        mandelbrotPanel = new MandelbrotPanel();
        maxIterations = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));

        maxIterations.addChangeListener(e -> {
            // Pass the selected depth to the fractal panel
            mandelbrotPanel.setMaxIterations((Integer) maxIterations.getValue());
            mandelbrotPanel.repaint(); // Repaint the panel to update the fractal
        });

        // Add Zoom Slider (JSlider for Zoom)
        zoomSlider = new JSlider(50, 1000, 200);  // Range from 50 to 1000 with initial zoom 200
        zoomSlider.addChangeListener(e -> {
            mandelbrotPanel.setZoom(zoomSlider.getValue());
            mandelbrotPanel.repaint();
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

                    mandelbrotPanel.repaint();
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
}