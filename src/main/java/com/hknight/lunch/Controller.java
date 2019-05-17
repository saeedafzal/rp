package com.hknight.lunch;

import javax.swing.*;

class Controller {

    String getInput() {
        return JOptionPane.showInputDialog("Name of place to add:");
    }

    static void log(Object msg) {
        System.out.println(msg);
    }
}
