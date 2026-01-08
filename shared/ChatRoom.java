package shared;

import util.GuiLogger;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChatRoom {
    private final String roomId;
    private final ConcurrentLinkedQueue<String> messageHistory;
    private boolean isActive;

    public ChatRoom() {
        this.roomId = generateRoomId();
        this.messageHistory = new ConcurrentLinkedQueue<>();
        this.isActive = true;
        GuiLogger.log("💬 Chat room created: " + roomId);
    }

    private String generateRoomId() {
        return "ROOM_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public void system(String message) {
        if (!isActive) return;

        String formattedMessage = "[System] " + message;
        messageHistory.add(formattedMessage);
        GuiLogger.log("[Room " + roomId + "] " + formattedMessage);
    }

    public void send(String sender, String message) {
        if (!isActive) return;

        String formattedMessage = "[" + sender + "] " + message;
        messageHistory.add(formattedMessage);
        GuiLogger.log("[Room " + roomId + "] " + formattedMessage);
    }

    public String getRoomId() {
        return roomId;
    }

    public void close() {
        isActive = false;
        GuiLogger.log(" Chat room closed: " + roomId);
    }

    public boolean isActive() {
        return isActive;
    }

    public String[] getMessageHistory() {
        return messageHistory.toArray(new String[0]);
    }
}