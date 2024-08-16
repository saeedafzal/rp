package com.saeed.rp.ui;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;
import static java.nio.file.Files.readAllLines;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static javax.swing.KeyStroke.getKeyStroke;

@Slf4j
public class MenuBar extends JMenuBar {

    private static final int MASK_KEY = getDefaultToolkit().getMenuShortcutKeyMaskEx();

    private final DefaultListModel<String> itemsListModel;
    private final Consumer<String> statusBarConsumer;
    private File currentFile;

    public MenuBar(DefaultListModel<String> itemsListModel, Consumer<String> statusBarConsumer) {
        this.itemsListModel = itemsListModel;
        this.statusBarConsumer = statusBarConsumer;

        add(fileMenu());
        add(helpMenu());
    }

    private JMenu fileMenu() {
        JMenu menu = new JMenu("File");

        JMenuItem saveAsItem = new JMenuItem("Save As");
        saveAsItem.setAccelerator(getKeyStroke('S', MASK_KEY + SHIFT_DOWN_MASK));
        saveAsItem.addActionListener(_ -> {
            if (!itemsListModel.isEmpty()) {
                saveAsAction();
            }
        });

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(getKeyStroke('S', MASK_KEY));
        saveItem.addActionListener(_ -> {
            if (itemsListModel.isEmpty()) {
                return;
            }

            if (currentFile == null) {
                saveAsAction();
            } else {
                writeToCurrentFile();
            }
        });

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setAccelerator(getKeyStroke('O', MASK_KEY));
        openItem.addActionListener(_ -> openFile());

        menu.add(saveItem);
        menu.add(saveAsItem);
        menu.add(openItem);
        return menu;
    }

    private JMenu helpMenu() {
        JMenu menu = new JMenu("Help");
        menu.add(new JMenuItem("Placeholder"));
        return menu;
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("RP files (.rp)", "rp"));
        return fileChooser;
    }

    private void saveAsAction() {
        JFileChooser fileChooser = createFileChooser();
        fileChooser.setDialogTitle("Save List");

        final int choice = fileChooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Ensure extension is present in filename
            if (!file.getName().toLowerCase().endsWith(".rp")) {
                file = new File(file.getAbsolutePath() + ".rp");
            }

            currentFile = file;
            writeToCurrentFile();
        }
    }

    private void writeToCurrentFile() {
        // Get content
        final String content = stream(itemsListModel.toArray())
            .map(Object::toString)
            .collect(joining("\n"));

        try (FileWriter writer = new FileWriter(currentFile)) {
            writer.write(content);
            statusBarConsumer.accept("Saved to file: " + currentFile.getName());
        } catch (IOException e) {
            log.error("Error saving file.", e);
            statusBarConsumer.accept("Error saving file: " + currentFile.getName());
        }
    }

    private void openFile() {
        JFileChooser fileChooser = createFileChooser();
        fileChooser.setDialogTitle("Open List");

        final int choice = fileChooser.showOpenDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION) {
            try {
                currentFile = fileChooser.getSelectedFile();
                readAllLines(currentFile.toPath())
                    .stream()
                    .peek(_ -> itemsListModel.clear())
                    .forEach(itemsListModel::addElement);
                statusBarConsumer.accept("Current File: " + currentFile.getName());
            } catch (IOException e) {
                log.error("Failed to open file.", e);
                statusBarConsumer.accept("Failed to open file " + currentFile.getName());
            }
        }
    }
}
