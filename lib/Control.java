package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Control {

    public Control(Model model) {
        this.model = model;
        this.state = new Select(model);
    }

    class StateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "line":
                    state = state.changeState(State.LINE);
                    break;
                case "rect":
                    state = state.changeState(State.RECT);
                    break;
                case "oval":
                    state = state.changeState(State.OVAL);
                    break;
                case "polyline":
                    state = state.changeState(State.POLYLINE);
                    break;
                case "polygon":
                    state = state.changeState(State.POLYGON);
                case "text":
                    state = state.changeState(State.TEXT);
            }
        }
    }

    class FilledButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "filled":
                    state = state.setFilled(true);
                    break;
                case "unfilled":
                    state = state.setFilled(false);
                    break;
            }
        }
    }

    class ColorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "black":
                    state = state.setColor(Color.BLACK);
                    break;
                case "blue":
                    state = state.setColor(Color.BLUE);
                    break;
                case "red":
                    state = state.setColor(Color.GREEN);
                    break;
                case "yellow":
                    state = state.setColor(Color.YELLOW);
                    break;
                case "green":
                    state = state.setColor(Color.GREEN);
                    break;
                case "cyan":
                    state = state.setColor(Color.CYAN);
                    break;
            }
        }
    }

    class StrokeFieldListener implements ActionListener {

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

    class TextChangeListen implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = JOptionPane.showInputDialog("Enter new text:");
            state = state.setText(str);
        }
    }

    class MouseActionListener implements MouseListener, MouseMotionListener {
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
            state = state.mousePressed(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }
    private Model model;
    private State state;
}
