package com.hknight.lunch;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class Controller {

    String getInput() {
        return JOptionPane.showInputDialog("Name of place to add:");
    }

    File saveWithChooser(final DefaultListModel listModel, final JFileChooser chooser, File currentSaveFile, final JFrame frame) {
        if (!listModel.isEmpty()) {
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setDialogTitle("Save List");
            final int val = chooser.showSaveDialog(frame);

            if (val == JFileChooser.APPROVE_OPTION) {
                try {
                    currentSaveFile = chooser.getSelectedFile();

                    writeToFile(listModel, currentSaveFile);
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }

        return currentSaveFile;
    }

    void writeToFile(final DefaultListModel listModel, final File currentSaveFile) throws IOException {
        String postFix = "";
        if (!currentSaveFile.getName().contains(".lunch")) {
            postFix = ".lunch";
        }
        final FileWriter writer = new FileWriter(currentSaveFile + postFix);
        for (int i=0; i < listModel.toArray().length; i++) {
            if (listModel.toArray().length == (i+1)) {
                writer.write((String) listModel.toArray()[i]);
            } else {
                writer.write(listModel.toArray()[i] + "\n");
            }
        }

        writer.close();
    }
}
