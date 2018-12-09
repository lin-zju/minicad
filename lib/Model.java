package lib;

import java.util.ArrayList;

public class Model {
    public ArrayList<Shape> getShapeList() {
        return shapeList;
    }

    public void modified() {
        view.modified();
    }

    private ArrayList<Shape> shapeList = new ArrayList<>();
    private View view;

}
