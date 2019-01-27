package lib;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

abstract public class State {
    public static final int LINE = 0;
    public static final int RECT = 1;
    public static final int OVAL = 2;
    public static final int POLYLINE = 3;
    public static final int POLYGON = 4;
    public static final int TEXT = 5;
    public static final int IMAGE = 6;
    public static final int SELECT = 7;

    public static final int LEFT_BUTTON = 0;
    public static final int RIGHT_BUTTON = 1;

    public static final int offset = 5;

    protected State(Model model) {
        this.shapeList = model.getShapeList();
        this.model = model;
    }

    protected State(State state) {
        this.filled = state.filled;
        this.color = state.color;
        this.stroke = state.stroke;
        this.model = state.model;
        this.shapeList = model.getShapeList();
    }


    public State changeState(int state) {
        HashMap<Integer, Class> constToClass = new HashMap<>();
        constToClass.put(LINE, DrawLine.class);
        constToClass.put(RECT, DrawRect.class);
        constToClass.put(OVAL, DrawOval.class);
        constToClass.put(POLYLINE, DrawPolyline.class);
        constToClass.put(POLYGON, DrawPolygon.class);
        constToClass.put(TEXT, DrawText.class);
        constToClass.put(IMAGE, DrawImage.class);
        constToClass.put(SELECT, Select.class);
        try {
            Constructor<State> ctor = constToClass.get(state).getConstructor(State.class);
            returnState = ctor.newInstance(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return returnState;
    }


    public State setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    public State setColor(Color color) {
        this.color = color;
        return this;
    }

    public State setStroke(float width) {
        this.stroke = stroke;
        return this;
    }

    public State makeLarger() {
        return this;
    }
    public State makeSmaller() {
        return this;
    }

    public State setText(String str) {
        return this;
    }

    public State mousePressed(MouseEvent e) {
        return this;
    }

    public State mouseMoved(MouseEvent e) {
        return this;
    }

    public State mouseDragged(MouseEvent e) {
        return this;
    }

    public State mouseReleased(MouseEvent e) {
        return this;
    }

    public State delete() {
        return this;
    }

    public State duplicate() { return this; }

    protected ArrayList<Shape> shapeList;
    protected boolean filled;
    protected Color color = Color.BLACK;
    protected float stroke = 10.0f;
    protected State returnState = this;
    protected Model model;
}

class DrawLine extends State {
//    public DrawLine(ArrayList<Shape> shapeList) {
//        super(shapeList);
//    }

    public DrawLine(State state) {
        super(state);
    }

    @Override
    public State mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            shapeList.add(new Line(e.getX(), e.getY(), e.getX(), e.getY()));
        }
        model.modified();
        return new Drawing(this);

    }
}
class DrawRect extends State {
//    public DrawRect(ArrayList<Shape> shapeList) {
//        super(shapeList);
//    }

    public DrawRect(State state) {
        super(state);
    }

    @Override
    public State mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            shapeList.add(new Rectangle(e.getX(), e.getY(), e.getX(), e.getY()));
        }
        model.modified();
        return new Drawing(this);
    }

}
class DrawOval extends State {
//    public DrawOval(ArrayList<Shape> shapeList) {
//        super(shapeList);
//    }

    public DrawOval(State state) {
        super(state);
    }

    @Override
    public State mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            shapeList.add(new Oval(e.getX(), e.getY(), e.getX(), e.getY()));
        }
        model.modified();
        return new Drawing(this);
    }
}
class DrawPolygon extends State {
//    public DrawPolygon(ArrayList<Shape> shapeList) {
//        super(shapeList);
//    }

    public DrawPolygon(State state) {
        super(state);
    }

    @Override
    public State mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            Polygon p = new Polygon();
            p.addPoint(e.getX(), e.getY());
            p.addPoint(e.getX(), e.getY());
            shapeList.add(p);
        }
        model.modified();
        return new DrawingPoly(this);
    }

}
class DrawPolyline extends State {
//    public DrawPolygon(ArrayList<Shape> shapeList) {
//        super(shapeList);
//    }

    public DrawPolyline(State state) {
        super(state);
    }

    @Override
    public State mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            Polyline p = new Polyline();
            p.addPoint(e.getX(), e.getY());
            p.addPoint(e.getX(), e.getY());
            shapeList.add(p);
        }
        model.modified();
        return new DrawingPoly(this);
    }
}
class DrawText extends State {

    public DrawText(State state) {
        super(state);
        this.text = JOptionPane.showInputDialog("Enter text:");
    }
    public State mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            shapeList.add(new Text(text, e.getX(), e.getY(), e.getX(), e.getY()));
        }
        model.modified();
        return new Drawing(this);
    }
    String text;

}

class DrawImage extends State {

    public DrawImage(State state) {
        super(state);
        setImage();
    }
    private void setImage() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images", "jpg", "png", "jpeg");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            image = icon;
        }
    }
    public State mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            shapeList.add(new Image(image, e.getX(), e.getY(), e.getX(), e.getY()));
        }
        model.modified();
        return new Drawing(this);
    }
    ImageIcon image;

}
class Drawing extends State {

    public Drawing(State state) {
        super(state);
        last = state;
        shape = shapeList.get(shapeList.size() - 1);
    }

    @Override
    public State mouseDragged(MouseEvent e) {
        super.mouseMoved(e);
        shape.setPoint(1, e.getX(), e.getY());
        model.modified();
        return this;
    }

    @Override
    public State mouseReleased(MouseEvent e) {
        return last;
    }

    State last;
    Shape shape;
}

class DrawingPoly extends State {
    public DrawingPoly(State state) {
        super(state);
        last = state;
        shape = (Poly)shapeList.get(shapeList.size() - 1);
    }

    @Override
    public State mouseMoved(MouseEvent e) {
        shape.setPoint(i, e.getX(), e.getY());
        model.modified();
        return this;
    }

    @Override
    public State mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            shape.addPoint(e.getX(), e.getY());
            i++;
            model.modified();
            return this;
        }
        else if (e.getButton() == MouseEvent.BUTTON3) {
            shape.removeLast();
            model.modified();
            return last;
        }
        return this;
    }

    State last;
    Poly shape;
    int i = 1;
}


class Select extends State {
    public Select(Model model) {
        super(model);
    }

    public Select(State state) {
        super(state);
    }

    public State setFilled(boolean filled) {
        super.setFilled(filled);
        if (current != null) {
            current.setFilled(filled);
            model.modified();
        }

        return this;
    }

    public State setColor(Color color) {
        super.setColor(color);
        if (current != null) {
            current.setColor(color);
            model.modified();
        }
        return this;
    }

    @Override
    public State setStroke(float width) {
        super.setStroke(width);
        if (current != null) {
            current.setStroke(width);
            model.modified();
        }
        return this;
    }

    @Override
    public State makeLarger() {
        if (current != null) {
            current.makeLarger(5);
            model.modified();
        }
        return this;
    }

    @Override
    public State makeSmaller() {
        if (current != null) {
            current.makeSmaller(5);
            model.modified();
        }
        return this;
    }

    @Override
    public State setText(String str) {
        if (current != null && current instanceof Text) {
            ((Text)current).setStr(str);
            model.modified();
        }
        return this;
    }

    @Override
    public State mousePressed(MouseEvent e) {
        boolean flag = false;
        lastX = e.getX();
        lastY = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            for (int i = shapeList.size() - 1; i >= 0; i--) {
                Shape shape = shapeList.get(i);
                if (shape.fallsWithin(e.getPoint())) {
                    flag = true;
                    current = shape;
                    currentI = i;
                    break;
                }
            }
        }
        if (!flag)
            current = null;
        return this;
    }


    @Override
    public State mouseDragged(MouseEvent e) {
        if (current != null) {
            current.move(e.getX() - lastX, e.getY() - lastY);
            lastX = e.getX();
            lastY = e.getY();
            model.modified();
        }
        return this;
    }

    @Override
    public State delete() {
        if (current != null)
            shapeList.remove(currentI);
        current = null;
        model.modified();
        return this;
    }

    @Override
    public State duplicate() {
        if (current != null) {
            try {
                shapeList.add((Shape)shapeList.get(currentI).clone());
                currentI = shapeList.size() - 1;
                current = shapeList.get(currentI);
                model.modified();
            }
            catch (CloneNotSupportedException e) { }
        }
        return this;
    }

    Shape current = null;
    int currentI = 0;
    int lastX;
    int lastY;

}