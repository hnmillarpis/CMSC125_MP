package threads;

import shared.MatchPool;
import shared.ChatRoom;
import util.GuiLogger;
import db.SupabaseClient;
import db.SupabaseWorker;

public class UserThread implements Runnable {

    private final String name;
    private final UserRole role;
    private final MatchPool pool;
    private ChatRoom chatRoom;
    private boolean isHumanUser;
    private boolean searching = true;


    private Thread thread;
    private UserThread matchedWith;


    public UserThread(String name, UserRole role, MatchPool pool, boolean isHumanUser) {
        this.name = name;
        this.role = role;
        this.pool = pool;
        this.isHumanUser = isHumanUser;
    }


    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            GuiLogger.log(name + " (" + role + ") joined matchmaking.");

            pool.enterPool(this);

            // WAIT for match
            synchronized (this) {
                while (matchedWith == null) {
                    wait(); // WAITING STATE
                }
            }

            searching = false;

            if (chatRoom == null || matchedWith == null) return;

            chatRoom.system("Chat started between " + name +
                    " and " + matchedWith.getName());

            // Auto-chat ONLY for bots
            if (!isHumanUser) {
                chatRoom.send(name, "Hello!");
                Thread.sleep(1000);
                chatRoom.send(name, "Nice to meet you!");
            }

            if (isHumanUser) {
                synchronized (this) {
                    while (matchedWith != null) {
                        wait(); // wait until Leave Chat
                    }
                }
            } else {
                // BOT stays alive until human leaves
                synchronized (this) {
                    while (matchedWith != null) {
                        wait();
                    }
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void setMatchedWith(UserThread other) {
        this.matchedWith = other;

        if (this.chatRoom == null) {
            this.chatRoom = new ChatRoom();
            other.chatRoom = this.chatRoom;
        }

        notify();
    }


    // CALLED ONLY BY MATCHMAKER
    public synchronized void setMatchedWith(UserThread other, ChatRoom room) {
        this.matchedWith = other;
        this.chatRoom = room;
        notify();
    }

    public UserRole getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
    public ChatRoom getChatRoom() {
        return chatRoom;
    }
    public synchronized void leaveChat() {
        GuiLogger.log("🚪 " + name + " left the chat.");

        matchedWith = null;
        chatRoom = null;

        notify(); // wake waiting thread
    }
    public UserThread getMatchedWith() {
        return matchedWith;
    }
    public boolean isSearching() {
        return searching;
    }


}
