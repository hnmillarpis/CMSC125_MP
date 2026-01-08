package gui;


import gui.MainApp;
import threads.UserRole;
import threads.UserThread;
import util.GuiLogger;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTextArea logArea;
    private JComboBox<String> roleBox;
    private JButton joinButton;
    private JTextField chatInput;
    private JButton sendButton;
    private UserThread currentUser;
    private JButton leaveButton;

    
    public MainFrame() {
        setTitle("💜 SwipeUP — CMSC 125");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        // Header
        JLabel header = new JLabel("SwipeUP — Multithreaded Matchmaking", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setForeground(new Color(128, 0, 128));
        add(header, BorderLayout.NORTH);

        // Chat log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);

        GuiLogger.init(logArea);


        // Handles roles and join
        JPanel controlPanel = new JPanel();
        roleBox = new JComboBox<>(new String[]{"STUDENT", "PROFESSOR"});
        joinButton = new JButton("Join Matchmaking");

        controlPanel.add(new JLabel("Role:"));
        controlPanel.add(roleBox);
        controlPanel.add(joinButton);
        leaveButton = new JButton("Leave Chat");
        controlPanel.add(leaveButton);
        leaveButton.addActionListener(e -> {
            if (currentUser != null) {
                currentUser.leaveChat();
                currentUser = null;
            }
        });


        // Chat space
        JPanel chatPanel = new JPanel();

        chatInput = new JTextField(30);
        sendButton = new JButton("Send");

        chatPanel.add(chatInput);
        chatPanel.add(sendButton);


        // South container
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(controlPanel, BorderLayout.NORTH);
        southPanel.add(chatPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Handles events
        joinButton.addActionListener(e -> {
            UserRole role = UserRole.valueOf(
                    (String) roleBox.getSelectedItem()
            );


            currentUser = new UserThread(
                    "User-" + System.currentTimeMillis(),
                    role,
                    MainApp.getPool(),
                    true
            );
            currentUser.start();


            showSearchingStatus(currentUser);


            // Bot (delayed join to simulate queue)
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // forces waiting phase
                    UserThread bot = new UserThread(
                            "User-" + System.nanoTime(),
                            role,
                            MainApp.getPool(),
                            false
                    );
                    bot.start();
                } catch (InterruptedException ignored) {}
            }).start();
        });

        // SEND BUTTON
        sendButton.addActionListener(e -> {
            if (currentUser != null && currentUser.getChatRoom() != null) {
                String msg = chatInput.getText().trim();
                if (!msg.isEmpty()) {


                    // Send user's message
                    currentUser.getChatRoom().send(
                            currentUser.getName(),
                            msg
                    );


                    // simulated reply
                    new Thread(() -> {
                        try {
                            Thread.sleep(800);
                            if (currentUser != null && currentUser.getChatRoom() != null) {
                                currentUser.getChatRoom().send(
                                        currentUser.getMatchedWith().getName(),
                                        "hello:)"
                                );
                            }
                        } catch (InterruptedException ignored) {}
                    }).start();


                    chatInput.setText("");
                }
            }
        });


    }
    private void showSearchingStatus(UserThread user) {
        new Thread(() -> {
            int seconds = 0;
            try {
                while (user != null && user.isSearching()) {
                    seconds++;
                    GuiLogger.log("🔍 Looking for a match... (" + seconds + "s)");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }


}

