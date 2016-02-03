
package Models;

public class ChatMessage {
    private final String message;
    private final String from, to;
    private boolean isRead;
    private final long time;
    
    public ChatMessage(String to, String from, String message) {
        this.message = message;
        this.from = from;
        this.to = to;
        isRead = false;
        time = System.currentTimeMillis() / 1000L;
    }
    
    public ChatMessage(String to, String from, String message, boolean isReaded, long time) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.isRead = isReaded;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public long getTime() {
        return time;
    }
    
    public void ReadMessage() {
        isRead = true;
    }

    @Override
    public String toString() {
        return "{\"message\":\"" + message + "\","
                + "\"from\":\"" + from + "\","
                + "\"to\":\"" + to + "\","
                + "\"isRead\":" + isRead + ","
                + "\"time\":" + time + '}';
    }
    
}
