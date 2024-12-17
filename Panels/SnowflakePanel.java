package Panels;

import javax.swing.*;
import java.awt.*;

public class SnowflakePanel extends JPanel {
    private int depth;

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        g.setColor(Color.BLUE);
        int width = getWidth();
        int height = getHeight();
        drawSnowflake(g, width / 2, height / 4, width / 2, depth);
    }

    private void drawSnowflake(Graphics g, int x, int y, int size, int depth) {
        // Initial triangle points
        int[] xPoints = {x, x - size, x + size};
        int[] yPoints = {y, y + size, y + size};

        // Draw the initial triangle
        drawKochCurve(g, xPoints[0], yPoints[0], xPoints[1], yPoints[1], depth);
        drawKochCurve(g, xPoints[1], yPoints[1], xPoints[2], yPoints[2], depth);
        drawKochCurve(g, xPoints[2], yPoints[2], xPoints[0], yPoints[0], depth);
    }

    private void drawKochCurve(Graphics g, int x1, int y1, int x2, int y2, int depth) {
        if (depth == 0) {
            g.drawLine(x1, y1, x2, y2);
        } else {
            // Calculate the points for the Koch curve
            int x3 = (x1 + x2) / 2;
            int y3 = (y1 + y2) / 2;
            int x4 = (x1 + x3) / 2;
            int y4 = (y1 + y3) / 2;
            int x5 = (x2 + x3) / 2;
            int y5 = (y2 + y3) / 2;

            // Create the "spike" point
            int x6 = (x4 + x5) / 2 - (y5 - y4) * (int) Math.sqrt(3) / 2;
            int y6 = (y4 + y5) / 2 + (x5 - x4) * (int) Math.sqrt(3) / 2;

            // Recursively draw the smaller Koch curves
            drawKochCurve(g, x1, y1, x4, y4, depth - 1);
            drawKochCurve(g, x4, y4, x6, y6, depth - 1);
            drawKochCurve(g, x6, y6, x5, y5, depth - 1);
            drawKochCurve(g, x5, y5, x2, y2, depth - 1);
        }
    }
}
