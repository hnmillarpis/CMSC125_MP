package gui;

import shared.MatchPool;
import threads.MatchMakerThread;
import db.SupabaseWorker;

import javax.swing.*;

public class MainApp {

    private static MatchPool pool;

    public static MatchPool getPool() {
        return pool;
    }

    public static void main(String[] args) {

        // Initialize shared match pool ONCE
        pool = new MatchPool();

        // Start matchmaker thread
        new MatchMakerThread(pool).start();

        // Start database worker
        SupabaseWorker dbWorker = new SupabaseWorker();
        dbWorker.start();

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
