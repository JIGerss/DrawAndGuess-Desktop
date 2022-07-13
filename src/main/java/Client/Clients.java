package Client;

import javax.swing.JOptionPane;

import com.alibaba.fastjson.*;
import processing.core.*;

import java.util.List;


public class Clients extends PApplet {
    private final int WIDTH = 900, HEIGHT = 550;
    public boolean isHost = false;
    public Game game;
    public User Player;
    public User[] players;

    public static void main(String[] args) {
        PApplet.main("Client.Clients");
    }

    public void setup() {
        stroke(20);
        size(WIDTH, HEIGHT);
        HttpRequest.doPost("http://101.34.38.133:8090/games/" + game.getId() + "/join", "", "");
        players = getUsersInGame();
    }

    public void draw() {
        background(253, 248, 229);
    }

    private Game getGame() {
        String json = HttpRequest.sendGet("http://101.34.38.133:8090/games", "");
        return JSON.parseObject(json, Game.class);
    }

    private User[] getUsersInGame() {
        String json = HttpRequest.sendGet("http://101.34.38.133:8090/games/" + game.getId() + "/players", "");
        List<String> list = JSON.parseArray(json, String.class);
        User[] USERS = new User[list.size()];
        int cur = 0;
        for (String s : list) {
            USERS[cur++] = JSON.parseObject(s, User.class);
        }
        return USERS;
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }
}




