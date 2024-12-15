package Fractals;

import javax.swing.*;
import java.awt.*;

public class TrianglePanel extends JPanel {
    private int depth;
    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        g.setColor(Color.BLUE);
        drawTriangle(g, getWidth() / 2, 50, 400, depth);
    }

    private void drawTriangle(Graphics g, int x, int y, int size, int depth) {
        if (depth == 0) {
            int[] xPoints = {x, x + size / 2, x - size / 2};
            int[] yPoints = {y, y + size, y + size};
            g.fillPolygon(xPoints, yPoints, 3);
        } else {
            drawTriangle(g, x, y, size / 2, depth - 1);
            drawTriangle(g, x - size / 4, y + size / 2, size / 2, depth - 1);
            drawTriangle(g, x + size / 4, y + size / 2, size / 2, depth - 1);
        }
    }
}
