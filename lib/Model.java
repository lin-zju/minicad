package lib;

import java.util.ArrayList;

public class Model {
    public ArrayList<Shape> getShapeList() {
        return shapeList;
    }

    public void modified() {
        view.modified();
    }
    public void setView(View view) {
        this.view = view;
    }

    public void setShapeList(ArrayList<Shape> shapeList) {
        this.shapeList = shapeList;
    }

    private ArrayList<Shape> shapeList = new ArrayList<>();
    private View view;
}
