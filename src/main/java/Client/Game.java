package Client;

public class Game {
    private String Id;
    private String Answer;
    private Line[] Lines;
    private User Drawer;
    private Message[] Messages;
    private boolean hasEnded;

    public Game(String id, String answer, Line[] lines, User drawer, Message[] messages, boolean hasEnded) {
        Id = id;
        Answer = answer;
        Lines = lines;
        Drawer = drawer;
        Messages = messages;
        this.hasEnded = hasEnded;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public Message[] getMessages() {
        return Messages;
    }

    public void setMessages(Message[] messages) {
        Messages = messages;
    }

    public Line[] getLines() {
        return Lines;
    }

    public void setLines(Line[] lines) {
        Lines = lines;
    }

    public boolean isHasEnded() {
        return hasEnded;
    }

    public void setHasEnded(boolean hasEnded) {
        this.hasEnded = hasEnded;
    }

    public User getDrawer() {
        return Drawer;
    }

    public void setDrawer(User drawer) {
        Drawer = drawer;
    }
}
