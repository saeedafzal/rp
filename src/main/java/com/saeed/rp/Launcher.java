package com.saeed.rp;

import com.saeed.rp.ui.WindowTwo;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

import static com.formdev.flatlaf.util.SystemInfo.isMacFullWindowContentSupported;

@Slf4j
public class Launcher {

    public void launch() {
        setup();
    }

    private void setup() {
        // MacOS specific
        if (isMacFullWindowContentSupported) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "RP");
            System.setProperty("apple.awt.application.appearance", "system");
        }

        // Set theme
        new ThemeHandler().init();

        // Show window
        SwingUtilities.invokeLater(WindowTwo::new);
    }
}
