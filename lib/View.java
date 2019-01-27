package lib;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    public View(Model model) {
        this.model = model;
    }
    public void modified() {
        repaint();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ArrayList<Shape> shapeList = model.getShapeList();
        for (Shape shape : shapeList) {
            shape.render((Graphics2D) g);
        }
    }

    Model model;
}
