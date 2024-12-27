package Panels;

import java.awt.*;
import javax.swing.*;

public class TrianglePanel extends JPanel {
    private int depth;

    private Color color = Color.BLUE;
    private Color backgroundColor = color.WHITE;

    // Setter for the depth of the fractal
    public void setDepth(int depth) {
        this.depth = depth;
        System.out.println("Depth set to: " + depth); // Debugging
        repaint();
    }
    public int getDepth() {
        return depth;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public void setBackgroundColor(Color color){
        this.backgroundColor= color;
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(backgroundColor); // Set background color
        g.setColor(color); // Set fractal color
        drawTriangle(g, getWidth() / 2, 50, 400, depth); // Draw the triangle
    }

    // Recursive method to draw the Sierpinski Triangle
    private void drawTriangle(Graphics g, int x, int y, int size, int depth) {
        if (depth == 0) {
            // Draw a filled triangle at the base case
            int[] xPoints = {x, x + size / 2, x - size / 2};
            int[] yPoints = {y, y + size, y + size};
            g.fillPolygon(xPoints, yPoints, 3);
        } else {
            // Draw smaller triangles at the recursive cases
            drawTriangle(g, x, y, size / 2, depth - 1);
            drawTriangle(g, x - size / 4, y + size / 2, size / 2, depth - 1);
            drawTriangle(g, x + size / 4, y + size / 2, size / 2, depth - 1);
        }
    }
}
