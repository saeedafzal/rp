package com.hknight.lunch;

import javax.swing.*;
import java.awt.*;

import static com.hknight.lunch.Controller.log;

class Window extends JFrame {

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final Controller controller = new Controller();
    private JList<String> list;
    private JLabel label;

    Window() {
        setTitle("Lunch Picker");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createControls());
        splitPane.setRightComponent(createRightPanel());

        contentPane.add(splitPane, BorderLayout.CENTER);
        setContentPane(contentPane);
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
