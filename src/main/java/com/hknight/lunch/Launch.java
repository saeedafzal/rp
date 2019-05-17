package com.hknight.lunch;

import javax.swing.*;

class Launch {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                final Window window = new Window();
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(window);
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
