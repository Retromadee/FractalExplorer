package Fractals;

import javax.swing.*;

public class FractalExplorer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SierpinskiTriangle app = new SierpinskiTriangle();
            app.setVisible(true);
        });
    }
}
