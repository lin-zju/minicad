package lib;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Control {

    public Control(Model model) {
        this.model = model;
        this.state = new Select(model);
        state = state.changeState(State.LINE);
    }

    private static HashMap<String, Color> strToColor = new HashMap<>();
    private static HashMap<String, Integer> strToState = new HashMap<>();

    static {
        strToColor.put("black", Color.BLACK);
        strToColor.put("blue", Color.BLUE);
        strToColor.put("red", Color.RED);
        strToColor.put("yellow", Color.YELLOW);
        strToColor.put("green", Color.GREEN);

        strToState.put("line", State.LINE);
        strToState.put("rect", State.RECT);
        strToState.put("oval", State.OVAL);
        strToState.put("polyline", State.POLYLINE);
        strToState.put("polygon", State.POLYGON);
        strToState.put("text", State.TEXT);
        strToState.put("image", State.IMAGE);
        strToState.put("select", State.SELECT);
    }

    public class StateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            state = state.changeState(strToState.get((e.getActionCommand())));
        }
    }

    public class FilledButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = e.getActionCommand();
            if (str.equals("filled"))
                state = state.setFilled(true);
            else if (str.equals("unfilled"))
                state = state.setFilled(false);
        }
    }

    public class ColorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            state.setColor(strToColor.get(e.getActionCommand()));
        }
    }

    public class StrokeFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            float width;
            try {
                width = Float.parseFloat(e.getActionCommand());
            }
            catch (NumberFormatException ex) {
                return;
            }
            state = state.setStroke(width);
        }
    }

    public class TextChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = JOptionPane.showInputDialog("Enter new text:");
            state = state.setText(str);
        }
    }

    public class MouseActionListener implements MouseListener, MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            state = state.mouseMoved(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            state = state.mouseDragged(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            state = state.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            state = state.mouseReleased(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    public class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            state = state.delete();
        }
    }

    public class SizeChangeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String str = e.getActionCommand();
            if (str.equals("larger"))
                state = state.makeLarger();
            else
                state = state.makeSmaller();
        }
    }

    public class DuplicateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            state = state.duplicate();
        }
    }

    public class SaveMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "miniCad file (*.cad)", "cad");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File raw = fileChooser.getSelectedFile();
                File file = new File(raw.getAbsolutePath() + ".cad");
                try {
                    ObjectOutputStream output =
                            new ObjectOutputStream((new FileOutputStream(file)));
                    for (Shape s : model.getShapeList()) {
                        output.writeObject(s);
                    }
                    output.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    System.out.println("unexpted IO error");
                    System.exit(0);
                }
            }
        }
    }

    public class OpenMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "miniCad file (*.cad)", "cad");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    ObjectInputStream input =
                            new ObjectInputStream(new FileInputStream(file));
                    ArrayList<Shape> shapeList = new ArrayList<>();
                    while (true) {
                        try {
                            Shape s = (Shape)input.readObject();
                            shapeList.add(s);
                        }
                        catch (EOFException e2) {
                            break;
                        }
                    }
                    model.setShapeList(shapeList);
                    model.modified();
                    state = state.changeState(State.SELECT);
                    input.close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                    System.out.println("unexpted IO error");
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    private Model model;
    private State state;
}
