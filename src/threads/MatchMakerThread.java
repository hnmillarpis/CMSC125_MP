package threads;

import shared.MatchPool;
import shared.ChatRoom;
import util.GuiLogger;
import db.SupabaseClient;
import db.SupabaseWorker;

public class MatchMakerThread extends Thread {

    private final MatchPool pool;

    public MatchMakerThread(MatchPool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        GuiLogger.log("🧠 MatchMaker started.");

        while (true) {
            synchronized (pool) {
                try {
                    UserThread[] match;

                    while ((match = pool.getMatchIfAvailable()) == null) {
                        pool.wait();
                    }

                    UserThread u1 = match[0];
                    UserThread u2 = match[1];

                    ChatRoom room = new ChatRoom(); // SHARED RESOURCE

                    GuiLogger.log("🔗 Matched: " +
                            u1.getName() + " ↔ " + u2.getName());

                    // Assign match + chat room
                    u1.setMatchedWith(u2, room);
                    u2.setMatchedWith(u1, room);

                    // DB write (async)
                    SupabaseWorker.submit(() -> {
                        SupabaseClient.post("/rest/v1/matches",
                                "{ \"user_a\": \"" + u1.getName() +
                                        "\", \"user_b\": \"" + u2.getName() + "\" }");
                    });

                } catch (InterruptedException e) {
                    interrupt();
                    break;
                }
            }
        }
    }
}
