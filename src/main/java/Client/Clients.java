package Client;

import com.alibaba.fastjson.*;
import processing.awt.PSurfaceAWT;
import processing.core.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Clients extends PApplet {
    public static final int WIDTH = 900, HEIGHT = 710, CANVAS_WIDTH = 350, CANVAS_HEIGHT = (int) (CANVAS_WIDTH / 9.0 * 18.0);
    private static final float leftX = (float) (WIDTH / 2 - CANVAS_WIDTH / 2), UpY = 8;
    public static boolean isDrawer = true;
    public static String gameId;
    public static User Player;
    private PImage logoutButton;
    private String URL = "\u001C\u0000\u0000\u0004N[[EDEZG@ZGLZEGGNLDMD[";
    private boolean isVisible = true;
    private boolean isDrawing = false;
    private boolean isOnButton = false;
    private int requestTime = 0, postTime = 0;
    private List<RelativePoint> offlinePoints;
    private List<Line> offlineLines;
    private String[] players;
    private Button logout;

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
        setVariables();
        logout = new Button(665, 430, 200, 50);
        logoutButton = loadImage("logout.png");
        logoutButton.resize(logout.width, logout.height);
        PFont myFont = createFont("DengXian", 32);
        textFont(myFont);
        frameRate(100);
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
        if (requestTime == 200) {
            requestTime = 0;
            players = getUsersInGame();
        } else {
            requestTime++;
        }
        isOnButton = false;
        background(253, 248, 229);
        stroke(148, 153, 160);
        fill(255, 255, 255);
        rect((float) (WIDTH / 2 - CANVAS_WIDTH / 2), UpY, CANVAS_WIDTH, CANVAS_HEIGHT);
        fill(24, 25, 28);
        stroke(24, 25, 28);
        if (isDrawer) {
            for (int i = 0; i < offlinePoints.size() - 1; i++)
                line(offlinePoints.get(i).getProcessedX(), offlinePoints.get(i).getProcessedY(), offlinePoints.get(i + 1).getProcessedX(), offlinePoints.get(i + 1).getProcessedY());
            if (isDrawing && postTime == 20) {
                offlineLines.add(packagePoints());
                postLines();
            }
        } else if (postTime == 20) {
            offlineLines = getLines();
        }
        if (postTime == 20) postTime = 0;
        else postTime ++;
        if (offlineLines != null) {
            for (Line offlineLine : offlineLines) {
                RelativePoint[] relativePoints = offlineLine.getPoints();
                for (int j = 0; j < relativePoints.length - 1; j++)
                    line(relativePoints[j].getProcessedX(), relativePoints[j].getProcessedY(), relativePoints[j + 1].getProcessedX(), relativePoints[j + 1].getProcessedY());
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

        if (isMovedOnButton(logout))
            fill(80, 80, 80);
        else
            fill(24, 25, 28);

        image(logoutButton, logout.x, logout.y);
        textSize(25);
        text("登出游戏", logout.x + 45, logout.y + 34);
        if (isOnButton) cursor(HAND);
        else cursor(ARROW);
    }

    public void mousePressed() {
        if (isOnCanvas() && isDrawer && !isDrawing) {
            isDrawing = true;
            RelativePoint[] points = {new RelativePoint(0, 0), new RelativePoint(0, 0)};
            offlineLines.add(new Line(points));
            offlinePoints.add(new RelativePoint(mouseX, mouseY));
        }
    }

    public void mouseClicked() {
        if (isMovedOnButton(logout)) {
            Object[] options = {"退出房间", "退出房间并退出游戏"};
            int op = JOptionPane.showOptionDialog(null, "退出房间 或 退出房间并退出游戏", "你画我猜 -退出房间", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            quitGame(op == 1);
        }
    }

    public void mouseReleased() {
        if (isDrawer) {
            isDrawing = false;
            offlineLines.add(packagePoints());
            offlinePoints = new ArrayList<>();
            postLines();
        }
    }

    private Line packagePoints() {
        RelativePoint[] relativePoints = new RelativePoint[offlinePoints.size()];
        int cur = 0;
        for (RelativePoint p : offlinePoints)
            relativePoints[cur++] = p;
        offlineLines.remove(offlineLines.size() - 1);
        offlineLines.add(new Line(relativePoints));
        return new Line(relativePoints);
    }

    private ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<>();
        String result = HttpRequest.sendGet(URL + "games/" + gameId + "/lines", "");
        List<String> list = JSON.parseArray(result, String.class);
        for (String s : list) {
            lines.add(JSON.parseObject(s, Line.class));
        }
        return lines;
    }

    private void postLines() {
        PostLine postLine = new PostLine(Player, offlineLines.get(offlineLines.size() - 1));
        String json = JSON.toJSONString(postLine);
        String result = HttpRequest.doPost(URL + "/games/" + gameId + "/lines", "", json);
        if (result == null) error();
    }

    private boolean isMovedOnButton(Button button) {
        if (mouseX > button.x && mouseX < button.x + button.width && mouseY > button.y && mouseY < button.y + button.height) {
            isOnButton = true;
            return true;
        }
        return false;
    }

    private boolean isOnCanvas() {
        return mouseX > leftX && mouseX < leftX + CANVAS_WIDTH
                && mouseY > UpY && mouseY < UpY + CANVAS_HEIGHT;
    }

    private void setVariables() {
        String result = null;
        offlinePoints = new ArrayList<>();
        offlineLines = new ArrayList<>();
        if (!isDrawer) {
            offlineLines = getLines();
        }
        if (!isDrawer) result = HttpRequest.doPost(URL + "games/" + gameId + "/join", "", JSON.toJSONString(Player));
        players = getUsersInGame();
        System.out.println("Succeed to join game!");
    }

    private void quitGame(boolean Logout) {
        String userJson = JSON.toJSONString(Player);
        String result = HttpRequest.doDelete(URL + "games/" + gameId + "/leave/" + Player.getUserName() + "/" + Player.getUserId(), "", userJson);
        System.out.println("Succeed to quit game!" + result);
        if (Logout) logout();
        Lobby.isGaming = false;
        isVisible = false;
        Frame frame = ((PSurfaceAWT.SmoothCanvas) surface.getNative()).getFrame();
        frame.setVisible(false);
    }

    private String[] getUsersInGame() {
        String json = HttpRequest.sendGet(URL + "games/" + gameId + "/players", "");
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
        System.exit(0);
    }

    private void error() {
        JOptionPane.showMessageDialog(null, "错误！！", "你画我猜", JOptionPane.WARNING_MESSAGE);
        exitAccidentally();
    }

    private void exitAccidentally() {
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




