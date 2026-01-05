package threads;

import shared.MatchPool;

public class UserThread implements Runnable {

    private final String name;
    private final UserRole role;
    private final MatchPool pool;
    private Thread thread;

    private UserThread matchedWith;

    public UserThread(String name, UserRole role, MatchPool pool) {
        this.name = name;
        this.role = role;
        this.pool = pool;
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            System.out.println("[" + Thread.currentThread().getName() +
                    "] " + name + " (" + role + ") joined matchmaking.");

            pool.enterPool(this);

            synchronized (this) {
                while (matchedWith == null) {
                    wait(); // BLOCKED STATE
                }
            }

            System.out.println("[" + Thread.currentThread().getName() +
                    "] Chat started between " + name + " and " + matchedWith.getName());

            Thread.sleep(2000); // simulate chat

            System.out.println("[" + Thread.currentThread().getName() +
                    "] Chat ended for " + name);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Called ONLY by MatchMaker inside critical section
    public synchronized void setMatchedWith(UserThread other) {
        this.matchedWith = other;
        notify(); // wake this user thread
    }

    public UserRole getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
