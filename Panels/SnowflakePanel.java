package Panels;

import java.awt.*;
import javax.swing.*;

public class SnowflakePanel extends JPanel {
    private int depth;
    private Color color = Color.BLUE;
    private Color backgroundColor = color.WHITE;

    public void setDepth(int depth) {
        this.depth = depth;
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
        setBackground(backgroundColor);

        g.setColor(color);

        // Get the window's width and height
        int width = getWidth();
        int height = getHeight();

        // Calculate the size of the snowflake
        int size = Math.min(width, height) /2;

        // Calculate the center position
        int centerX = width / 2;
        int centerY = height / 5;

        // Draw the snowflake
        drawSnowflake(g, centerX, centerY, size, depth);
    }

    
    



    private void drawSnowflake(Graphics g, int x, int y, int size, int depth) {
        int halfSize = size / 2;
        int height = (int) (Math.sqrt(3) * halfSize);
    
        int x1 = x;
        int y1 = y;
    
        int x2 = x - halfSize;
        int y2 = y + height;
    
        int x3 = x + halfSize;
        int y3 = y + height;
    
        drawKochCurve(g, x1, y1, x2, y2, depth);
        drawKochCurve(g, x2, y2, x3, y3, depth);
        drawKochCurve(g, x3, y3, x1, y1, depth);
    }
    

    private void drawKochCurve(Graphics g, int x1, int y1, int x2, int y2, int depth) {
        if (depth == 0) {
            g.drawLine(x1, y1, x2, y2);
        } else {
            double x3 = (2 * x1 + x2) / 3.0;
            double y3 = (2 * y1 + y2) / 3.0;
            double x4 = (x1 + 2 * x2) / 3.0;
            double y4 = (y1 + 2 * y2) / 3.0;
    
            double dx = x4 - x3;
            double dy = y4 - y3;
            double x5 = x3 + dx / 2 - dy * Math.sqrt(3) / 2;
            double y5 = y3 + dy / 2 + dx * Math.sqrt(3) / 2;
    
            drawKochCurve(g, x1, y1, (int) x3, (int) y3, depth - 1);
            drawKochCurve(g, (int) x3, (int) y3, (int) x5, (int) y5, depth - 1);
            drawKochCurve(g, (int) x5, (int) y5, (int) x4, (int) y4, depth - 1);
            drawKochCurve(g, (int) x4, (int) y4, x2, y2, depth - 1);
        }
    }
    
}
