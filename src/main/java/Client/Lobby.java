package Client;

import org.apache.commons.codec.digest.DigestUtils;
import com.alibaba.fastjson.JSON;
import processing.awt.PSurfaceAWT;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Lobby extends PApplet {
    public static boolean isGaming = false;
    private final int WIDTH = 900, HEIGHT = 550, CHOICES = 5;
    private final Room[] rooms = new Room[6];
    private PImage logoutButton;
    private String URL = "\u001C\u0000\u0000\u0004N[[EDEZG@ZGLZEGGNLDMD[";
    private boolean isVisible = true;
    private boolean isOnButton = false;
    private boolean hasStartAGame = false;
    private int gameNum = 0;
    private int requestTime = 60;
    private String[] vocabs;
    private String[] users;
    private Button logout;
    private User Player;
    private Game[] games;

    public static void main(String[] args) {
        PApplet.main("Client.Lobby");
    }

    private String encryptToMD5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public void setup() {
        size(WIDTH, HEIGHT);
        URL = convertMD5(URL);
        boolean isSucceed = false;
        while (!isSucceed) {
            //Enter account
            String name = JOptionPane.showInputDialog(null, "输入用户名：", "你画我猜", JOptionPane.INFORMATION_MESSAGE);
            if (name == null || name.equals("")) exitLobby();
            String RegTest = HttpRequest.sendGet(URL + "users/hasreg/" + name, "");

            boolean isCorrect = false;
            while (!isCorrect) {
                //Enter password
                String psw;
                if (RegTest.equals("N")) {
                    psw = JOptionPane.showInputDialog(null, "请设置新用户" + name + "的密码：", "你画我猜 -注册", JOptionPane.INFORMATION_MESSAGE);
                    if (psw == null) break;
                    if (psw.equals("")) continue;
                    HttpRequest.doPost(URL + "users/reg/" + name + "/" + encryptToMD5(psw), "", "");
                    String login = HttpRequest.doPost(URL + "users/login/" + name + "/" + encryptToMD5(psw), "", "");
                    Player = JSON.parseObject(login, User.class);
                    isCorrect = true;
                    isSucceed = true;
                } else {
                    psw = JOptionPane.showInputDialog(null, "请输入用户" + name + "的密码：", "你画我猜 -登录", JOptionPane.INFORMATION_MESSAGE);
                    if (psw == null) break;
                    if (psw.equals("")) continue;
                    String loginTest = HttpRequest.doPost(URL + "users/login/" + name + "/" + encryptToMD5(psw), "", "");
                    if (loginTest != null) {
                        if (loginTest.contains("password is incorrect")) {
                            JOptionPane.showMessageDialog(null, "密码错误！", "你画我猜 -密码错误", JOptionPane.WARNING_MESSAGE);
                        } else if (loginTest.contains("Already Exists")) {
                            JOptionPane.showMessageDialog(null, "该账号已登录！", "你画我猜 -账号已登录", JOptionPane.WARNING_MESSAGE);
                            break;
                        } else {
                            System.out.println(loginTest);
                            Player = JSON.parseObject(loginTest, User.class);
                            isCorrect = true;
                            isSucceed = true;
                        }
                    } else
                        error();
                }
            }
        }
        setVariables();
        vocabs = getVocabs();
        logout = new Button(665, 430, 200, 50);
        System.out.println("Succeed to login!Hello " + Player.getUserName() + " " + Player.getUserId());
        logoutButton = loadImage("logout.png");
        logoutButton.resize(logout.width, logout.height);
        PFont myFont = createFont("DengXian", 32);
        textFont(myFont);
        strokeWeight(2);
        frameRate = 60;
    }

    public void draw() {
        if (!isVisible && !isGaming) {
            Frame frame = ((PSurfaceAWT.SmoothCanvas) surface.getNative()).getFrame();
            frame.setVisible(true);
            isVisible = true;
            setVariables();
        } else if (isGaming && !isVisible) {
            return;
        }
        if (requestTime == 60) {
            requestTime = 0;
            setVariables();
        } else {
            requestTime++;
        }
        isOnButton = false;
        background(253, 248, 200);
        textSize(30);
        fill(241, 242, 243);
        rect(640, 5, 250, 580, 20);

        textSize(25);
        fill(24, 25, 28);
        text("在线玩家", 660, 50);
        textSize(17);
        fill(148, 153, 160);
        text("(" + users.length + ")：", 770, 50);
        fill(24, 25, 28);
        textSize(25);
        text("点击房间进入游戏：", 30, 50);
        text(Player.getUserName(), 670, 90);

        stroke(200, 200, 200);
        for (int i = 0; i < 7; i++) {
            line(660, 100 + i * 40, 865, 100 + i * 40);
        }
        for (int i = 0, j = 0; i < users.length; i++, j++) {
            if (j >= 5) {
                text("···········", 670, 130 + i * 40);
                break;
            }
            if (!users[i].equals(Player.getUserName())) {
                String name = users[i];
                if (name.length() > 16)
                    name = name.substring(0, 13) + "...";
                text(name, 670, 130 + j * 40);
            } else
                j--;
        }
        for (Room room : rooms) {
            if (isMovedOnButton(room.button))
                fill(190, 163, 162);
            else {
                if (!room.isGame)
                    fill(230, 198, 26);
                else
                    fill(200, 198, 26);
            }
            rect(room.button.x, room.button.y, room.button.width, room.button.height, 50);
            fill(24, 25, 28);
            text(room.numberOfPlayer, room.button.x + 40, room.button.y + 59);
            if (room.isGame) {
                textSize(17);
                fill(148, 153, 130);
                text("房主", room.button.x - 70, room.button.y + 29);
                textSize(25);
                fill(24, 25, 28);
                String name = room.game.getDrawerName();
                if (name.length() > 8)
                    name = name.substring(0, 5) + "...";
                while (name.length() < 8)
                    name = " " + name;
                text(name, room.button.x - 100, room.button.y + 59);
            } else
                text("        空", room.button.x - 100, room.button.y + 59);
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
        setVariables();
        if (isMovedOnButton(logout)) {
            logout();
            exitLobby();
        }
        for (Room room : rooms) {
            if (isMovedOnButton(room.button)) {
                if (!room.isGame) {
                    Game game = createGame();
                    System.out.println(JSON.toJSONString(game));
                    if (game != null) {
                        System.out.println("Set Answer to " + game.getAnswer());
                        joinGame(game, Player, true);
                    }
                } else {
                    joinGame(room.game, Player, false);
                }
            }
        }
    }

    private boolean isMovedOnButton(Button button) {
        if (mouseX > button.x && mouseX < button.x + button.width && mouseY > button.y && mouseY < button.y + button.height) {
            isOnButton = true;
            return true;
        }
        return false;
    }

    private void setVariables() {
        users = getUsers();
        games = getGames();
        gameNum = games.length;
        setRooms();
    }

    private Game createGame() {
        try {
            String[] choices = getRandomAnswers();
            String answer = (String) JOptionPane.showInputDialog(null, "请选择房间题目：", "你画我猜 -选择题目", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
            if (answer == null) return null;
            String jsonStr = JSON.toJSONString(Player);
            String result = HttpRequest.doPost(URL + "games/create/" + answer, "", jsonStr);
            return JSON.parseObject(result, Game.class);
        } catch (com.alibaba.fastjson.JSONException e) {
            error();
        }
        return null;
    }

    private void joinGame(Game game, User Player, boolean isHost) {
        isGaming = true;
        isVisible = false;
        Frame frame = ((PSurfaceAWT.SmoothCanvas) surface.getNative()).getFrame();
        frame.setVisible(false);
        Clients.isDrawer = isHost;
        Clients.gameId = game.getId();
        Clients.Player = Player;
        if (!hasStartAGame) {
            hasStartAGame = true;
            Clients.main(args);
        }
    }

    private String[] getRandomAnswers() {
        String[] VOCABS = new String[CHOICES];
        Random random = new Random(System.currentTimeMillis());
        int MAX = vocabs.length - 1, MIN = 0, cur = 0;
        for (int i = 0; i < CHOICES; i++) {
            int number = random.nextInt(MAX - MIN + 1) + MIN;
            VOCABS[cur++] = vocabs[number];
        }
        return VOCABS;
    }

    private void setRooms() {
        for (int i = 0; i < rooms.length; i++) {
            if (gameNum > i) {
                rooms[i] = new Room(new Button(130 + (i % 2) * 300, 85 + (i / 2) * 150, 100, 100), games[i]);
                String json = HttpRequest.sendGet(URL + "games/" + games[i].getId() + "/players", "");
                List<String> list = JSON.parseArray(json, String.class);
                rooms[i].numberOfPlayer = list.size();
                rooms[i].isGame = true;
            } else {
                rooms[i] = new Room(new Button(130 + (i % 2) * 300, 85 + (i / 2) * 150, 100, 100));
            }
        }
    }

    private void logout() {
        String userJson = JSON.toJSONString(Player);
        String result = HttpRequest.doDelete(URL + "users/logout/" + Player.getUserName() + "/" + Player.getUserId(), "", userJson);
        System.out.println("Succeed to logout!" + result);
    }

    private String[] getUsers() {
        String json = HttpRequest.sendGet(URL + "users/list", "");
        List<String> list = JSON.parseArray(json, String.class);
        String[] USERS = new String[list.size()];
        int cur = 0;
        for (String s : list)
            USERS[cur++] = s;
        return USERS;
    }

    private Game[] getGames() {
        String json = HttpRequest.sendGet(URL + "games", "");
        List<String> list = JSON.parseArray(json, String.class);
        int size = 0;
        for (String s : list)
            if (!JSON.parseObject(s, Game.class).isEnd()) size++;
        Game[] GAMES = new Game[size];
        int cur = 0;
        for (String s : list) {
            Game temp = JSON.parseObject(s, Game.class);
            if (!temp.isEnd()) GAMES[cur++] = temp;
        }
        return GAMES;
    }

    private String[] getVocabs() {
        String json = HttpRequest.sendGet(URL + "vocabs", "");
        List<String> list = JSON.parseArray(json, String.class);
        String[] VOCABS = new String[list.size()];
        int cur = 0;
        for (String s : list)
            VOCABS[cur++] = s;
        return VOCABS;
    }

    private String convertMD5(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }

    private void exitLobby() {
        try {
            System.exit(0);
        } catch (SecurityException securityException) {
            System.out.println("Error!!");
        }
    }

    private void error() {
        JOptionPane.showMessageDialog(null, "错误！！", "你画我猜", JOptionPane.WARNING_MESSAGE);
        exitLobby();
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

}
