package com.saeed.rp.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Supplier;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class Window extends JFrame {

    private final DefaultListModel<String> itemsListModel;
    private final JList<String> itemList;
    private final JLabel resultLabel;
    private final JLabel statusBarLabel;
    private final Supplier<String> addItemDialogSupplier;

    public Window() {
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

        setTitle("Random Picker UI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setJMenuBar(new RPMenuBar(itemsListModel, this::setStatusBarText));

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.15);
        mainSplitPane.setLeftComponent(buttonMenuPanel());
        mainSplitPane.setRightComponent(mainPanel());

        contentPane.add(mainSplitPane, BorderLayout.CENTER);
        contentPane.add(statusBarPanel(), BorderLayout.SOUTH);
        setContentPane(contentPane);
    }

    private JPanel buttonMenuPanel() {
        JPanel buttonMenuPanel = new JPanel();
        buttonMenuPanel.setLayout(new GridLayout(5, 1));

        JButton addItemBtn = new JButton("Add");
        addItemBtn.setFocusPainted(false);
        addItemBtn.addActionListener(e -> itemsListModel.addElement(addItemDialogSupplier.get()));

        JButton removeItemBtn = new JButton("Remove");
        removeItemBtn.setFocusPainted(false);
        removeItemBtn.addActionListener(e -> itemsListModel.removeElement(itemList.getSelectedValue()));

        JButton clearItemBtn = new JButton("Clear");
        clearItemBtn.setFocusPainted(false);
        clearItemBtn.addActionListener(e -> {
            itemsListModel.clear();

        });

        JButton pickItemBtn = new JButton("Pick");
        pickItemBtn.setFocusPainted(false);
        pickItemBtn.addActionListener(e -> {
            if (itemsListModel.size() > 0) {
                final int random = (int) (Math.random() * itemsListModel.size());
                final String pick = itemsListModel.get(random);
                resultLabel.setText(pick);
            }
        });

        JButton exitItemBtn = new JButton("Exit");
        exitItemBtn.setFocusPainted(false);
        exitItemBtn.addActionListener(e -> System.exit(0));

        buttonMenuPanel.add(addItemBtn);
        buttonMenuPanel.add(removeItemBtn);
        buttonMenuPanel.add(clearItemBtn);
        buttonMenuPanel.add(pickItemBtn);
        buttonMenuPanel.add(exitItemBtn);
        return buttonMenuPanel;
    }

    private JPanel mainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setTopComponent(itemsListPanel());
        splitPane.setBottomComponent(resultPanel());

        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel itemsListPanel() {
        JPanel itemsListPanel = new JPanel();
        itemsListPanel.setLayout(new BorderLayout());

        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        itemsListPanel.add(itemList, BorderLayout.CENTER);
        return itemsListPanel;
    }

    private JPanel resultPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());

        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        resultPanel.add(resultLabel, BorderLayout.CENTER);
        return resultPanel;
    }

    private JPanel statusBarPanel() {
        JPanel statusBarPanel = new JPanel();
        statusBarPanel.setLayout(new BorderLayout());

        statusBarLabel.setHorizontalAlignment(SwingConstants.LEFT);

        statusBarPanel.add(statusBarLabel, BorderLayout.CENTER);
        return statusBarPanel;
    }

    private void setStatusBarText(String text) {
        statusBarLabel.setText(text);
    }
}
