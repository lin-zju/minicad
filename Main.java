import lib.Control;
import lib.Model;
import lib.View;
import lib.Window;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View(model);
        Control control = new Control(model);
        model.setView(view);
        Window window = new Window(view, control);
        window.setSize(1000, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}
