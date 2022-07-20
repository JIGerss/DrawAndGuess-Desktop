package Structures;

public class PostLine {
    private User from;
    private Line newLine;

    public PostLine(User from, Line newLine) {
        this.from = from;
        this.newLine = newLine;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Line getNewLine() {
        return newLine;
    }

    public void setNewLine(Line newLine) {
        this.newLine = newLine;
    }
}
