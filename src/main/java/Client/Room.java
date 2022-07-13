package Client;

public class Room {
    private static int num = 0;
    public int number;
    public int numberOfPlayer;
    public boolean isGame;
    public Game game;
    public Button button;

    public Room(Button button, Game game) {
        this.button = button;
        this.game = game;
        isGame = true;
        number = num++;
        numberOfPlayer = 0;
    }

    public Room(Button button) {
        this.button = button;
        isGame = false;
        number = num++;
        numberOfPlayer = 0;
    }
}
