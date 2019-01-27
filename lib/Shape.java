package lib;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

abstract public class Shape implements Serializable, Cloneable {
    public void render(Graphics2D g) {
        g.setStroke(new BasicStroke(stroke));
        g.setColor(color);
    }
    abstract public boolean fallsWithin(Point point);
    public Point findCenter() {
        if (points.isEmpty())
            return new Point(0, 0);
        double x = 0;
        double y = 0;
        for (Point point : points) {
            x += point.getX();
            y += point.getY();
        }

        return new Point((int)(x / points.size()), (int)(y / points.size()));
    }
    public void makeSmaller(int offset) {
        double factor = findScale(offset, SMALLER);
        scaleShape(factor);
    }

    public void makeLarger(int offset) {
        double factor = findScale(offset, LARGER);
//        System.out.println(factor);
        scaleShape(factor);
    }

    public void move(int dx, int dy) {
        for (Point p : points) {
            p.translate(dx, dy);
        }
    }
    public void setPoint(int index, int x, int y) {
        Point p = points.get(index);
        p.setLocation(x, y);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStroke(float width) {
        this.stroke = width;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    // private part
    protected ArrayList<Point> points = new ArrayList<>();
    private Color color = Color.BLACK;
    private float stroke = 2.0f;
    protected boolean filled = false;

    private static final int LARGER = 0;
    private static final int SMALLER = 1;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    protected static final int OFFSET = 10;


    private Point findVectorFromCenter(Point p) {
        Point center = findCenter();
        return new Point(p.x - center.x, p.y - center.y);
    }
    private double findScale(int offset, int mode) {
        int max = 0;
        for (Point point : points) {
            Point vec = findVectorFromCenter(point);
            int maxXY =  Math.abs(Math.max(vec.x, vec.y));
//            System.out.println(maxXY);
            max = Math.max(max, maxXY);
        }
//        System.out.println(offset);
//        System.out.println(max);
        if (mode == LARGER)
            return (offset + max) / (double)max;
        if (mode == SMALLER) {
            if (max - offset > 0)
                return (max - offset) / (double)max;
            else
                return 1.0;
        }
        else
            return 1.0;
    }
    private void scaleShape(double factor) {
        Point center = findCenter();
        for (Point p: points) {
            int dx = (int)(factor * (p.getX() - center.x));
            int dy = (int)(factor * (p.getY() - center.y));
            p.setLocation(center.x + dx, center.y + dy);
        }
    }
    protected static double findDistanceToLine(Point p1, Point p2, Point p) {
        double x1 = p1.getX(), y1 = p1.getY(), z1 = 1.0;
        double x2 = p2.getX(), y2 = p2.getY(), z2 = 1.0;
        double a = y1 * z2 - y2 * z1;
        double b = -(x1 * z2 - x2 * z1);
        double c = x1 * y2 - x2 * y1;
        double x = p.getX(), y = p.getY(), z = 1.0;
        return (a * x + b * y + z * c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }
    protected int getMinX() {
        int min = 10000;
        for (Point p : points) {
            min = Math.min(min, p.x);
        }
        return min;
    }
    protected int getMinY() {
        int min = 10000;
        for (Point p : points) {
            min = Math.min(min, p.y);
        }
        return min;
    }
    protected int getMaxX() {
        int max = 0;
        for (Point p : points) {
            max = Math.max(max, p.x);
        }
        return max;
    }
    protected int getMaxY() {
        int max = 0;
        for (Point p : points) {
            max = Math.max(max, p.y);
        }
        return max;
    }
    protected boolean fallsWithinBoundingBox(Point p) {
        if (p.x >= getMinX() && p.x <= getMaxX() &&
            p.y >= getMinY() && p.y <= getMaxY())
            return true;
        else
            return false;
    }

    @Override
    abstract protected Object clone() throws CloneNotSupportedException;

}

class Line extends Shape {
    public Line(int x1, int y1, int x2, int y2) {
        points.add(new Point(x1, y1));
        points.add(new Point(x2, y2));
    }
    @Override
    public void render(Graphics2D g) {
        super.render(g);
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Line newLine = new Line(0, 0, 0, 0);
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            newLine.setPoint(i, p.x + OFFSET, p.y);
        }
        return newLine;
    }

    @Override
    public boolean fallsWithin(Point p) {
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        if (p.x >= Math.min(p1.x, p2.x) && p.x <= Math.max(p1.x, p2.x) &&
            p.y >= Math.min(p1.y, p2.y) && p.y <= Math.max(p1.y, p2.y) &&
            findDistanceToLine(p1, p2, p) < threshold) {
            return true;
        }
        else
            return false;
    }
    private final static double threshold = 10.0;
}

class Rectangle extends Shape {
    public Rectangle(int x1, int y1, int x2, int y2) {
        points.add(new Point(x1, y1));
        points.add(new Point(x2, y2));
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        int x = getMinX(), y = getMinY(), w = getMaxX() - x , h = getMaxY() - y;
        if (filled) {
            g.fillRect(x, y, w, h);
        }
        else {
            g.drawRect(x, y, w, h);
        }
    }
    @Override
    public boolean fallsWithin(Point p) {
        return fallsWithinBoundingBox(p);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Rectangle newRect = new Rectangle(0, 0, 0, 0);
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            newRect.setPoint(i, p.x + OFFSET, p.y);
        }
        return newRect;
    }
}

class Oval extends Shape {
    public Oval(int x1, int y1, int x2, int y2) {
        points.add(new Point(x1, y1));
        points.add(new Point(x2, y2));
    }

    public void render(Graphics2D g) {
        super.render(g);
        int x = getMinX(), y = getMinY(), w = getMaxX() - x , h = getMaxY() - y;
        if (filled) {
            g.fillOval(x, y, w, h);
        }
        else {
            g.drawOval(x, y, w, h);
        }
    }
    @Override
    public boolean fallsWithin(Point p) {
        return fallsWithinBoundingBox(p);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Oval newOval = new Oval(0, 0, 0, 0);
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            newOval.setPoint(i, p.x + OFFSET, p.y);
        }
        return newOval;
    }
}

abstract class Poly extends Shape {
    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }
    public void removeLast() {
        points.remove(points.size() - 1);
    }
}

class Polygon extends Poly {
    public Polygon() { }
    public void render(Graphics2D g) {
        super.render(g);
        java.awt.Polygon polygon = new java.awt.Polygon();
        for (Point p : points) {
            polygon.addPoint(p.x, p.y);
        }
        g.drawPolygon(polygon);
    }

    @Override
    public boolean fallsWithin(Point p) {
        boolean close = false;
        for (int i = 0; i < points.size(); i++) {
            int j = (i == points.size() - 1) ? 0 : i + 1;
            if (findDistanceToLine(points.get(i), points.get(j), p) < threshold) {
                close = true;
                break;
            }
        }
        if (close && fallsWithinBoundingBox(p))
            return true;
        else
            return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Polygon newPoly = new Polygon();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            newPoly.addPoint(p.x + OFFSET, p.y);
        }
        return newPoly;
    }
    private static final double threshold = 10.0;
}

class Polyline extends Poly {
    public Polyline() { }
    public void render(Graphics2D g) {
        super.render(g);
        ArrayList<Integer> xList = new ArrayList<Integer>();
        ArrayList<Integer> yList = new ArrayList<Integer>();
        for (Point p : points) {
            xList.add(p.x);
            yList.add(p.y);
        }
        int[] xPoints = new int[xList.size()];
        int[] yPoints = new int[yList.size()];
        for (int i = 0; i < points.size(); i++) {
            xPoints[i] = xList.get(i);
            yPoints[i] = yList.get(i);
        }
        g.drawPolyline(xPoints, yPoints, points.size());
    }

    @Override
    public boolean fallsWithin(Point p) {
        boolean close = false;
        for (int i = 0; i < points.size() - 1; i++) {
            int j = i + 1;
            if (findDistanceToLine(points.get(i), points.get(j), p) < threshold) {
                close = true;
                break;
            }
        }
        if (close && fallsWithinBoundingBox(p))
            return true;
        else
            return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Polyline newPoly = new Polyline();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            newPoly.addPoint(p.x + OFFSET, p.y);
        }
        return newPoly;
    }


    private static final double threshold = 10.0;
}


class Text extends Shape {
    public Text(String str, int x1, int y1, int x2, int y2) {
        this.str = str;
        points.add(new Point(x1, y1));
        points.add(new Point(x2, y2));
    }

    @Override
    public void render(Graphics2D g) {
        if (str == null)
            return;
        super.render(g);
        Font font = new Font("Californian FB", Font.BOLD, getMaxY() - getMinY());
        g.setFont(font);
        g.drawString(str, getMinX(), getMaxY());
    }
    @Override
    public boolean fallsWithin(Point p) {
        return fallsWithinBoundingBox(p);
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Text newText = new Text(str, 0, 0, 0, 0);
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            newText.setPoint(i, p.x + OFFSET, p.y);
        }
        return newText;
    }
    private String str;

}

class Image extends Shape {
    public Image(ImageIcon image, int x1, int y1, int x2, int y2) {
        this.image = image;
        points.add(new Point(x1, y1));
        points.add(new Point(x2, y2));
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if (image != null)
            g.drawImage(image.getImage(), getMinX(), getMinY(), getMaxX() - getMinX(), getMaxY() - getMinY(), null);
    }
    @Override
    public boolean fallsWithin(Point p) {
        return fallsWithinBoundingBox(p);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Image newImage = new Image(image, 0, 0, 0, 0);
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            newImage.setPoint(i, p.x + OFFSET, p.y);
        }
        return newImage;
    }


    private ImageIcon image;
}