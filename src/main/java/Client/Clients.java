package Client;

import com.alibaba.fastjson.*;
import processing.awt.PSurfaceAWT;
import processing.core.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Clients extends PApplet {
    public static final int WIDTH = 900, HEIGHT = 710;
    public static boolean isDrawer = true;
    public static String gameId;
    public static User Player;
    private String URL = "\u001C\u0000\u0000\u0004N[[EDEZG@ZGLZEGGNLDMD[";    public static final int CANVAS_WIDTH = 350, CANVAS_HEIGHT = (int) (CANVAS_WIDTH / 9.0 * 18.0);
    private boolean isVisible = true;
    private boolean isDrawing = false;    private static final float leftX = (float) (WIDTH / 2 - CANVAS_WIDTH / 2), UpY = 8;
    private int requestTime = 60;
    private List<RelativePoint> offlinePoints;
    private List<Line> offlineLines;
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
        offlineLines = new ArrayList<>();
        //setVariables();
        frameRate(100);
        background(253, 248, 229);
        stroke(148, 153, 160);
        fill(255, 255, 255);
        rect((float) (WIDTH / 2 - CANVAS_WIDTH / 2), UpY, CANVAS_WIDTH, CANVAS_HEIGHT);
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
        if (requestTime == 120) {
            requestTime = 0;
//            game = getGame();
//            players = getUsersInGame();
        } else {
            requestTime++;
        }

//        background(253, 248, 229);
//        stroke(148, 153, 160);
//        fill(255, 255, 255);
//        rect((float) (WIDTH / 2 - CANVAS_WIDTH / 2), UpY, CANVAS_WIDTH, CANVAS_HEIGHT);
        fill(24, 25, 28);
        stroke(24, 25, 28);
        if (isDrawer && isDrawing) {
            for (int i = 0; i < offlinePoints.size() - 1; i++)
                line(offlinePoints.get(i).getX(), offlinePoints.get(i).getY(), offlinePoints.get(i + 1).getX(), offlinePoints.get(i + 1).getY());
            for (Line offlineLine : offlineLines) {
                RelativePoint[] relativePoints = offlineLine.getPoints();
                for (int j = 0; j < relativePoints.length - 1; j++)
                    line(relativePoints[j].getX(), relativePoints[j].getY(), relativePoints[j + 1].getX(), relativePoints[j + 1].getY());
            }
        }

        if (isDrawing) {
            if (isOnCanvas())
                offlinePoints.add(new RelativePoint(mouseX, mouseY));
            else {
                float x = mouseX, y = mouseY;
                if (mouseX < leftX || mouseX > leftX + CANVAS_WIDTH)
                    x = (mouseX <= leftX) ? (leftX + 2) : (leftX + CANVAS_WIDTH - 2);
                if (mouseY < UpY || mouseY > UpY + CANVAS_HEIGHT)
                    y = (mouseY < UpY) ? (UpY + 2) : (UpY + CANVAS_HEIGHT - 2);
                offlinePoints.add(new RelativePoint(x, y));
            }
        }

    }

    public void mousePressed() {
        if (isOnCanvas()) {
            if (isDrawer && !isDrawing) {
                isDrawing = true;
                offlinePoints.add(new RelativePoint(mouseX, mouseY));
            }
        }
//        quitGame();
    }

    public void mouseReleased() {
        isDrawing = false;
        RelativePoint[] relativePoints = new RelativePoint[offlinePoints.size()];
        int cur = 0;
        for (RelativePoint p : offlinePoints)
            relativePoints[cur++] = p;
        offlineLines.add(new Line(relativePoints));
        offlinePoints = new ArrayList<>();
    }

    private boolean isOnCanvas() {
        return mouseX > leftX && mouseX < leftX + CANVAS_WIDTH
                && mouseY > UpY && mouseY < UpY + CANVAS_HEIGHT;
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




