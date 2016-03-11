package tdfpro.twitchplays;

public class IRCMessage {
    private final String sender;
    private final String message;

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public IRCMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;

    }
}
