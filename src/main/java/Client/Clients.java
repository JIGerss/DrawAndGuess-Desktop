package Client;

import com.alibaba.fastjson.*;
import processing.awt.PSurfaceAWT;
import processing.core.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class Clients extends PApplet {
    public static boolean isDrawer = false;
    public static String gameId;
    public static User Player;
    private final int WIDTH = 900, HEIGHT = 710;
    private String URL = "\u001C\u0000\u0000\u0004N[[EDEZG@ZGLZEGGNLDMD[";
    private boolean isVisible = true;    public static final int CANVAS_WIDTH = 350, CANVAS_HEIGHT = (int) (CANVAS_WIDTH / 9.0 * 18.0);
    private boolean isDrawing = false;
    private int requestTime = 60, postTime = 5;
    private RelativePoint lastPoint;
    private String[] players;
    private Game game;

    public static void main(String[] args) {
        PApplet.main("Client.Clients");
    }

    private String convertMD5(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }

    public void setup() {
        stroke(20);
        size(WIDTH, HEIGHT);
        URL = convertMD5(URL);
        //setVariables();
        frameRate(60);
    }

    public void draw() {
        if (Lobby.isGaming && !isVisible) {
            Frame frame = ((PSurfaceAWT.SmoothCanvas) surface.getNative()).getFrame();
            frame.setVisible(true);
            isVisible = true;
            setVariables();
        } else if (!Lobby.isGaming && !isVisible) {
            return;
        }
        if (requestTime == 60) {
            requestTime = 0;
//            game = getGame();
//            players = getUsersInGame();
        } else {
            requestTime++;
        }

        background(253, 248, 229);
        stroke(148, 153, 160);
        fill(255, 255, 255);
        rect((float) (WIDTH / 2 - CANVAS_WIDTH / 2), 8, CANVAS_WIDTH, CANVAS_HEIGHT);
        if (isDrawing && postTime == 5) {
            postTime = 0;
            fill();
            line(lastPoint.getX(), lastPoint.getY(), mouseX, mouseY);
        } else {
            postTime++;
        }

    }

    public void mousePressed() {
        if (mouseX > (float) (WIDTH / 2 - CANVAS_WIDTH / 2) && mouseX < (float) (WIDTH / 2 + CANVAS_WIDTH / 2)
                && mouseY > 8 && mouseY < 8 + CANVAS_HEIGHT) {
            isDrawing = isDrawer;
            lastPoint = new RelativePoint(mouseX, mouseY);
        }
//        quitGame();
    }

    private void setVariables() {
        String result = null;
        if (!isDrawer) result = HttpRequest.doPost(URL + "games/" + gameId + "/join", "", JSON.toJSONString(Player));
        game = getGame();
        players = getUsersInGame();
        System.out.println("Succeed to join game! " + result);
    }

    private Game getGame() {
        String json = HttpRequest.sendGet(URL + "games/" + gameId, "");
        return JSON.parseObject(json, Game.class);
    }

    private void quitGame() {
        String userJson = JSON.toJSONString(Player);
        String result = HttpRequest.doDelete(URL + "games/" + game.getId() + "/leave/" + Player.getUserName() + "/" + Player.getUserId(), "", userJson);
        System.out.println("Succeed to quit game!" + result);
        Lobby.isGaming = false;
        isVisible = false;
        Frame frame = ((PSurfaceAWT.SmoothCanvas) surface.getNative()).getFrame();
        frame.setVisible(false);
    }

    private String[] getUsersInGame() {
        String json = HttpRequest.sendGet(URL + "games/" + game.getId() + "/players", "");
        List<String> list = JSON.parseArray(json, String.class);
        String[] USERNAMES = new String[list.size()];
        int cur = 0;
        for (String s : list)
            USERNAMES[cur++] = s;
        return USERNAMES;
    }

    private void logout() {
        String userJson = JSON.toJSONString(Player);
        String result = HttpRequest.doDelete(URL + "users/logout/" + Player.getUserName() + "/" + Player.getUserId(), "", userJson);
        System.out.println("Succeed to logout!" + result);
    }

    private void error() {
        JOptionPane.showMessageDialog(null, "错误！！", "你画我猜", JOptionPane.WARNING_MESSAGE);
        exitGame();
    }

    private void exitGame() {
        try {
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }




}




