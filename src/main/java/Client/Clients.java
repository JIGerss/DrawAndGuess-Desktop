package Client;

import com.alibaba.fastjson.*;
import processing.core.*;

import java.util.List;


public class Clients extends PApplet {
    private final int WIDTH = 900, HEIGHT = 550;
    public boolean isDrawer = false;
    public String gameId;
    public User Player;
    private String[] players;
    private Game game;

    public static void main(String[] args) {
        PApplet.main("Client.Clients");
    }

    public void setup() {
        stroke(20);
        size(WIDTH, HEIGHT);
        HttpRequest.doPost("http://101.34.38.133:8090/games/" + gameId + "/join", "", "");
        players = getUsersInGame();
        game = getGame();
    }

    public void draw() {
        background(253, 248, 229);
    }

    private Game getGame() {
        String json = HttpRequest.sendGet("http://101.34.38.133:8090/games/" + gameId, "");
        return JSON.parseObject(json, Game.class);
    }

    private String[] getUsersInGame() {
        String json = HttpRequest.sendGet("http://101.34.38.133:8090/games/" + game.getId() + "/players", "");
        List<String> list = JSON.parseArray(json, String.class);
        String[] USERNAMES = new String[list.size()];
        int cur = 0;
        for (String s : list)
            USERNAMES[cur++] = s;
        return USERNAMES;
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }
}




