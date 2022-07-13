package Client;

public class Game {
    private String id;
    private String answer;
    private Line[] lines;
    private String drawerName;
    private Message[] messages;
    private boolean IsEnd;

    public Game(String id, String answer, Line[] lines, String drawerName, Message[] messages, boolean isEnd) {
        this.id = id;
        this.answer = answer;
        this.lines = lines;
        this.drawerName = drawerName;
        this.messages = messages;
        IsEnd = isEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Line[] getLines() {
        return lines;
    }

    public void setLines(Line[] lines) {
        this.lines = lines;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public void setDrawerName(String drawerName) {
        this.drawerName = drawerName;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    public boolean isEnd() {
        return IsEnd;
    }

    public void setEnd(boolean end) {
        IsEnd = end;
    }
}
