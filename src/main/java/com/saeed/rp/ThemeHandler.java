package com.saeed.rp;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.jthemedetecor.OsThemeDetector;
import lombok.extern.slf4j.Slf4j;

import static com.formdev.flatlaf.FlatLaf.updateUILater;
import static com.formdev.flatlaf.util.SystemInfo.isMacFullWindowContentSupported;
import static com.jthemedetecor.OsThemeDetector.getDetector;

@Slf4j
public class ThemeHandler {

    private final OsThemeDetector detector = getDetector();

    public void init() {
        setTheme(detector.isDark());
        detector.registerListener(this::setTheme);
    }

    private void setTheme(boolean isDark) {
        log.info("Changing theme: isDark={}", isDark);

        if (isDark) {
            setDarkTheme();
        } else {
            setLightTheme();
        }

        updateUILater();
    }

    private void setLightTheme() {
        if (isMacFullWindowContentSupported) {
            FlatLaf.setup(new FlatMacLightLaf());
        } else {
            FlatLaf.setup(new FlatLightLaf());
        }
    }

    private void setDarkTheme() {
        if (isMacFullWindowContentSupported) {
            FlatLaf.setup(new FlatMacDarkLaf());
        } else {
            FlatLaf.setup(new FlatDarkLaf());
        }
    }
}
