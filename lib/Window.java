package lib;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window(View view, Control control) {
        setLayout(new BorderLayout());
        // view
        add(view, BorderLayout.CENTER);
        view.addMouseListener(control.new MouseActionListener());
        view.addMouseMotionListener(control.new MouseActionListener());

        // state panel
        JPanel statePanel = new JPanel();
        add(statePanel, BorderLayout.WEST);
        statePanel.setLayout(new GridLayout(0, 1));
        JButton[] stateBtns = {
                new JButton("select"),
                new JButton("line"),
                new JButton("rect"),
                new JButton("oval"),
                new JButton("polyline"),
                new JButton("polygon"),
                new JButton("text"),
                new JButton("image")
        };
        for (JButton btn : stateBtns) {
            btn.addActionListener(control.new StateButtonListener());
            statePanel.add(btn);
        }

        // optionPanel
        JPanel optionPanel = new JPanel();
        add(optionPanel, BorderLayout.EAST);
        optionPanel.setLayout(new GridLayout(0, 1));

        // delete
        JButton deleteButton = new JButton("delete");
        optionPanel.add(deleteButton);
        deleteButton.addActionListener(control.new DeleteListener());

        // duplicate
        JButton duplicateButton = new JButton("duplicate");
        optionPanel.add(duplicateButton);
        duplicateButton.addActionListener(control.new DuplicateListener());

        // filled/unfilled
        JPanel filledPanel = new JPanel();
        optionPanel.add(filledPanel);

        filledPanel.setLayout(new GridLayout(1, 0));
        JButton[] filledBtns = {
                new JButton("filled"),
                new JButton("unfilled")
        };
        for (JButton btn: filledBtns) {
            btn.addActionListener(control.new FilledButtonListener());
            filledPanel.add(btn);
        };

        // size
        JPanel sizePanel = new JPanel();
        optionPanel.add(sizePanel);
        sizePanel.setLayout(new GridLayout(1, 0));
        JButton[] sizeButtons = {
                new JButton("larger"),
                new JButton("smaller")
        };

        for (JButton btn: sizeButtons) {
            btn.addActionListener(control.new SizeChangeListener());
            sizePanel.add(btn);
        }


        // color
        JPanel colorPanel = new JPanel();
        optionPanel.add(colorPanel);
        colorPanel.setLayout(new GridLayout(3, 0));
        JButton[] colorBtns = {
                new JButton("black"),
                new JButton("red"),
                new JButton("green"),
                new JButton("yellow"),
                new JButton("blue")
        };
        for (JButton btn: colorBtns) {
            btn.addActionListener(control.new ColorButtonListener());
            colorPanel.add(btn);
        };

        // text
        JButton textButton = new JButton("new text");
        optionPanel.add(textButton);
        textButton.addActionListener(control.new TextChangeListener());

        // stroke
        JPanel strokePanel = new JPanel();
        optionPanel.add(strokePanel);
        strokePanel.setLayout(new GridLayout(1, 0));

        JLabel strokeLabel = new JLabel("stroke:");
        strokePanel.add(strokeLabel);
        JTextField strokeField = new JTextField(4);
        strokePanel.add(strokeField);
        strokeField.addActionListener(control.new StrokeFieldListener());



        // menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem loadItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        saveItem.addActionListener(control.new SaveMenuListener());
        loadItem.addActionListener(control.new OpenMenuListener());

    }

    public static void main(String[] args) {
//        Model model = new Model();
//        View view = new View(model);
//        Control control = new Control(model);
//        model.setView(view);
//        Window window = new Window(view, control);
//        window.setSize(1000, 600);
//        window.setLocationRelativeTo(null);
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.setVisible(true);
    }
}
