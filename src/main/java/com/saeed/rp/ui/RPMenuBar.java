package com.saeed.rp.ui;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

public class RPMenuBar extends JMenuBar {

    private static final int MASK_KEY = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
    private final JFileChooser fileChooser;
    private final DefaultListModel<String> itemsListModel;
    private final Consumer<String> statusBarConsumer;
    private File currentFile;

    public RPMenuBar(DefaultListModel<String> itemsListModel, Consumer<String> statusBarConsumer) {
        this.itemsListModel = itemsListModel;
        this.statusBarConsumer = statusBarConsumer;
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Lunch Lists (.lunch)", "lunch"));

        add(fileMenu());
    }

    private JMenu fileMenu() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke('S', MASK_KEY + InputEvent.SHIFT_DOWN_MASK));
        saveAsItem.addActionListener(e -> {
            if (itemsListModel.getSize() > 0) {
                saveAsAction();
            }
        });

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S', MASK_KEY));
        saveItem.addActionListener(e -> {
            if (currentFile == null && itemsListModel.getSize() > 0) {
                saveAsAction();
            } else {
                writeToCurrentFile("");
            }
        });

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke('O', MASK_KEY));
        openItem.addActionListener(e -> {
            fileChooser.setDialogTitle("Open List");
            final int choice = fileChooser.showOpenDialog(this);
            if (choice == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                try (Stream<String> stream = Files.lines(Paths.get(currentFile.getPath()))) {
                    itemsListModel.clear();
                    stream.forEach(itemsListModel::addElement);
                    statusBarConsumer.accept("Current File: " + currentFile.getName());
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        });

        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(openItem);
        return fileMenu;
    }

    private void saveAsAction() {
        fileChooser.setDialogTitle("Save List");
        final int choice = fileChooser.showSaveDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            String postfix = "";
            if (!file.getName().endsWith(".lunch")) {
                postfix = ".lunch";
            }

            currentFile = file;
            writeToCurrentFile(postfix);

            statusBarConsumer.accept("Saved to file: " + currentFile.getName());
        }
    }

    private void writeToCurrentFile(String postfix) {
        if (currentFile != null) {
            try (FileWriter writer = new FileWriter(currentFile + postfix)) {
                for (int i = 0; i < itemsListModel.getSize(); i++) {
                    writer.write(itemsListModel.getElementAt(i) + "\n");
                }
                statusBarConsumer.accept("Current File: " + currentFile.getName());
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }
}
