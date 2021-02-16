package com.saeed.rp;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.saeed.rp.ui.Window;

import static javax.swing.UIManager.getSystemLookAndFeelClassName;

public class Launch {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(getSystemLookAndFeelClassName());

                final Window window = new Window();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
