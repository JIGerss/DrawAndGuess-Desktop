package Client;

import com.alibaba.fastjson.*;
import processing.awt.PSurfaceAWT;
import processing.core.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Clients extends PApplet {
    public static final int CANVAS_WIDTH = 350, CANVAS_HEIGHT = (int) (CANVAS_WIDTH / 9.0 * 18.0);
    public static final int WIDTH = 900, HEIGHT = 710;
    public static boolean isDrawer = true;
    public static String gameId;
    public static User Player;
    private String URL = "\u001C\u0000\u0000\u0004N[[EDEZG@ZGLZEGGNLDMD[";
    private boolean isVisible = true;
    private boolean isDrawing = false;
    private int requestTime = 60, postTime = 5;
    private List<RelativePoint> offlinePoints;
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
        strokeWeight(2);
        size(WIDTH, HEIGHT);
        URL = convertMD5(URL);
        offlinePoints = new ArrayList<>();
        //setVariables();
        frameRate(60);
        background(253, 248, 229);
        stroke(148, 153, 160);
        fill(255, 255, 255);
        rect((float) (WIDTH / 2 - CANVAS_WIDTH / 2), 8, CANVAS_WIDTH, CANVAS_HEIGHT);
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

//        background(253, 248, 229);
//        stroke(148, 153, 160);
//        fill(255, 255, 255);
//        rect((float) (WIDTH / 2 - CANVAS_WIDTH / 2), 8, CANVAS_WIDTH, CANVAS_HEIGHT);
        fill(24, 25, 28);
        stroke(24, 25, 28);
        if (isDrawer && offlinePoints != null) {
            for (int i = 0; i < offlinePoints.size() - 1; i++) {
                line(offlinePoints.get(i).getX(), offlinePoints.get(i).getY(), offlinePoints.get(i + 1).getX(), offlinePoints.get(i + 1).getY());
            }
        }
        if (isDrawing && postTime == 5) {
            postTime = 0;
            line(offlinePoints.get(offlinePoints.size() - 1).getX(), offlinePoints.get(offlinePoints.size() - 1).getY(), mouseX, mouseY);
            offlinePoints.add(new RelativePoint(mouseX, mouseY));
        } else {
            postTime++;
        }
        System.out.println(isDrawing);
    }

    public void mousePressed() {
        if (mouseX > (float) (WIDTH / 2 - CANVAS_WIDTH / 2) && mouseX < (float) (WIDTH / 2 + CANVAS_WIDTH / 2)
                && mouseY > 8 && mouseY < 8 + CANVAS_HEIGHT) {
            if (isDrawer && !isDrawing) {
                isDrawing = true;
                offlinePoints.add(new RelativePoint(mouseX, mouseY));
            }
        }
//        quitGame();
    }

//    public void mouseDragged() {
//        if (mouseX > (float) (WIDTH / 2 - CANVAS_WIDTH / 2) && mouseX < (float) (WIDTH / 2 + CANVAS_WIDTH / 2)
//                && mouseY > 8 && mouseY < 8 + CANVAS_HEIGHT) {
//            if (isDrawer && !isDrawing) {
//                isDrawing = true;
//                offlinePoints.add(new RelativePoint(mouseX, mouseY));
//            }
//        }
//    }

    public void mouseReleased() {
        isDrawing = false;
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




