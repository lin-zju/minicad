package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

abstract public class State {
    public static final int LINE = 0;
    public static final int RECT = 1;
    public static final int OVAL = 2;
    public static final int POLYLINE = 3;
    public static final int POLYGON = 4;
    public static final int TEXT = 5;

    public static final int LEFT_BUTTON = 0;
    public static final int RIGHT_BUTTON = 1;

    public static final int offset = 5;

    protected State(Model model) {
        this.shapeList = model.getShapeList();
        this.model = model;
    }

    protected State(State state) {
        this.shapeList = state.shapeList;
        this.filled = state.filled;
        this.color = state.color;
        this.stroke = state.stroke;
        this.model = model;
    }

    public State changeState(int state) {
        switch (state) {
            case LINE:
                returnState = new DrawLine(this);
                break;
            case RECT:
                returnState = new DrawRect(this);
                break;
            case OVAL:
                returnState = new DrawOval(this);
                break;
            case POLYLINE:
                returnState = new DrawPolyline(this);
                break;
            case POLYGON:
                returnState = new DrawPolygon(this);
                break;
            case TEXT:
                returnState = new DrawText(this);
                break;
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
            shapeList.add(new Rectangle(e.getX(), e.getY(), e.getX(), e.getY()));
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
//    public DrawText(ArrayList<Shape> shapeList) {
//        super(shapeList);
//    }

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
        return new DrawingPoly(this);
    }
    String text;

}

class Drawing extends State {

    public Drawing(State state) {
        super(state);
        last = state;
        shape = shapeList.get(shapeList.size() - 1);
    }

    @Override
    public State mouseMoved(MouseEvent e) {
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
            return this;
        }
        else if (e.getButton() == MouseEvent.BUTTON3) {
            shape.removeLast();
            return last;
        }
        model.modified();
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
            current.setFilled(true);
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
            for (Shape shape : shapeList) {
                if (shape.fallsWithin(e.getPoint())) {
                    flag = true;
                    current = shape;
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
        }
        return this;
    }

    Shape current = null;
    int lastX;
    int lastY;

}