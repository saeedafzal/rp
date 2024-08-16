package com.saeed.rp.ui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static com.formdev.flatlaf.util.SystemInfo.isMacFullWindowContentSupported;

public class WindowTwo extends JFrame {

    private static final String TITLE = "RP";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private final DefaultListModel<String> itemsListModel;
    private final JList<String> itemList;
    private final JLabel resultLabel;
    private final JLabel statusBarLabel;
    private final Supplier<String> addItemDialogSupplier;

    public WindowTwo() {
        itemsListModel = new DefaultListModel<>();
        itemList = new JList<>(itemsListModel);
        resultLabel = new JLabel("Add item...");
        statusBarLabel = new JLabel("No file chosen");
        addItemDialogSupplier = () -> JOptionPane.showInputDialog(
            this,
            "Enter a value:",
            "New Item",
            JOptionPane.INFORMATION_MESSAGE
        );

        if (isMacFullWindowContentSupported) {
            JRootPane rootPane = getRootPane();
            rootPane.putClientProperty("apple.awt.transparentTitleBar", true);
        }

        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setJMenuBar(new MenuBar(itemsListModel, this::setStatusBarText));

        setContentPane(contentPane());
        setVisible(true);
    }

    private JPanel contentPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(mainSplitPane(), BorderLayout.CENTER);
        panel.add(statusBarPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JSplitPane mainSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.15);
        splitPane.setLeftComponent(buttonMenuPanel());
        splitPane.setRightComponent(mainPanel());
        return splitPane;
    }

    private JPanel buttonMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JButton addItemBtn = new JButton("Add");
        addItemBtn.addActionListener(_ -> itemsListModel.addElement(addItemDialogSupplier.get()));

        JButton removeItemBtn = new JButton("Remove");
        removeItemBtn.addActionListener(_ -> itemsListModel.removeElement(itemList.getSelectedValue()));

        JButton clearItemBtn = new JButton("Clear");
        clearItemBtn.addActionListener(_ -> itemsListModel.clear());

        JButton pickItemBtn = new JButton("Pick");
        pickItemBtn.addActionListener(_ -> {
            if (!itemsListModel.isEmpty()) {
                int index = ThreadLocalRandom.current().nextInt(itemsListModel.size());
                String pick = itemsListModel.get(index);
                resultLabel.setText(pick);
            }
        });

        JButton exitItemBtn = new JButton("Exit");
        exitItemBtn.addActionListener(_ -> System.exit(0));

        panel.add(addItemBtn);
        panel.add(removeItemBtn);
        panel.add(clearItemBtn);
        panel.add(pickItemBtn);
        panel.add(exitItemBtn);
        return panel;
    }

    private JPanel mainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setTopComponent(itemsListPanel());
        splitPane.setBottomComponent(resultPanel());

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel itemsListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(itemList, BorderLayout.CENTER);
        return panel;
    }

    private JPanel resultPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(resultLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel statusBarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        statusBarLabel.setHorizontalAlignment(SwingConstants.LEFT);

        panel.add(statusBarLabel, BorderLayout.CENTER);
        return panel;
    }

    private void setStatusBarText(String text) {
        statusBarLabel.setText(text);
    }
}
