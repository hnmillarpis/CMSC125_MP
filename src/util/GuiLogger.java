package util;

import javax.swing.*;

public class GuiLogger {

    private static JTextArea outputArea;

    public static void init(JTextArea area) {
        outputArea = area;
    }

    public static void log(String message) {
        if (outputArea == null) return;

        SwingUtilities.invokeLater(() -> {
            outputArea.append(message + "\n");
        });
    }
}



