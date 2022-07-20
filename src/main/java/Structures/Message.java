package Structures;

import Structures.User;

public class Message {
    private User From;
    private String Content;

    public Message(User from, String content) {
        From = from;
        Content = content;
    }


    public User getFrom() {
        return From;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setFrom(User from) {
        From = from;
    }
}
