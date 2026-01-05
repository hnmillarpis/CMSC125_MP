package threads;

import shared.MatchPool;

public class MatchMakerThread extends Thread {

    private final MatchPool pool;
    private volatile boolean running = true;

    public MatchMakerThread(MatchPool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        System.out.println("[MATCHMAKER] Started.");

        while (running) {
            synchronized (pool) {
                try {
                    UserThread[] match;

                    while ((match = pool.getMatchIfAvailable()) == null) {
                        pool.wait(); // BLOCK until producer adds user
                    }

                    UserThread u1 = match[0];
                    UserThread u2 = match[1];

                    System.out.println("[MATCHMAKER] Matched: " +
                            u1.getName() + " ↔ " + u2.getName());

                    u1.setMatchedWith(u2);
                    u2.setMatchedWith(u1);

                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }

    public void shutdown() {
        running = false;
        interrupt();
    }
}
