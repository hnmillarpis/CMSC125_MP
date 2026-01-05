package gui;

import util.GuiLogger;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextArea logArea;

    public MainFrame() {
        setTitle("💜 SwipeUP — CMSC 125");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(logArea);

        JLabel header = new JLabel("SwipeUP — Multithreaded Matchmaking", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setForeground(new Color(128, 0, 128));

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        GuiLogger.init(logArea);
    }
}