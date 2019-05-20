package com.hknight.lunch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

class Window extends JFrame {

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final Controller controller = new Controller();
    private JList<String> list;
    private JLabel label;
    private File currentSaveFile;

    Window() {
        setTitle("Lunch Picker");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setJMenuBar(createMenuBar());

        final JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createControls());
        splitPane.setRightComponent(createRightPanel());

        contentPane.add(splitPane, BorderLayout.CENTER);
        setContentPane(contentPane);
    }

    private JMenuBar createMenuBar() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperties().getProperty("user.home")));

        final JMenuBar menuBar = new JMenuBar();

        final JMenu fileMenu = new JMenu("File");
        final JMenuItem saveAsMenu = new JMenuItem("Save As...");
        saveAsMenu.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + ActionEvent.SHIFT_MASK));
        saveAsMenu.addActionListener(e -> currentSaveFile = controller.saveWithChooser(listModel, chooser, currentSaveFile, this));

        final JMenuItem saveMenu = new JMenuItem("Save");
        saveMenu.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveMenu.addActionListener(e -> {
            if (currentSaveFile != null) {
                try {
                    controller.writeToFile(listModel, currentSaveFile);
                } catch (IOException io) {
                    io.printStackTrace();
                }
            } else {
                currentSaveFile = controller.saveWithChooser(listModel, chooser, currentSaveFile, this);
            }
        });

        final JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveMenu);
        fileMenu.add(saveAsMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        return menuBar;
    }

    private JPanel createControls() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        final JButton addPlaceBtn = new JButton("Add");
        addPlaceBtn.addActionListener(e -> {
            final String place = controller.getInput();
            if (!listModel.contains(place)) {
                listModel.addElement(place);
            }
        });

        final JButton removePlaceBtn = new JButton("Remove");
        removePlaceBtn.addActionListener(e -> {
            final int index = list.getSelectedIndex();
            if (index != -1) {
                listModel.removeElementAt(index);
            }

        });

        final JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            listModel.clear();
            label.setText("Start adding places.");
        });

        final JButton pickPlaceBtn = new JButton("Pick");
        pickPlaceBtn.addActionListener(e -> {
            final int random = (int) (Math.random() * listModel.size());
            final String pick = listModel.get(random);

            label.setText(pick);
        });

        final JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(addPlaceBtn);
        panel.add(removePlaceBtn);
        panel.add(clearBtn);
        panel.add(pickPlaceBtn);
        panel.add(exitBtn);
        return panel;
    }

    private JPanel createRightPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setTopComponent(createList());
        splitPane.setBottomComponent(createView());

        panel.add(splitPane);
        return panel;
    }

    private JList createList() {
        list = new JList<>(listModel);
        list.setDragEnabled(false);
        return list;
    }

    private JPanel createView() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        label = new JLabel("Start adding places.");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
