package Client;

public class User {
    private String gameId;
    private String userName;
    private String userId;

    public User(){
        gameId = "00000000-0000-0000-0000-000000000000";
        userId = "00000000-0000-0000-0000-000000000000";
        userName = "Default";
    }

    public User(String gameId, String userName, String userId) {
        this.gameId = gameId;
        this.userName = userName;
        this.userId = userId;
    }

    public User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
        gameId = "00000000-0000-0000-0000-000000000000";
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
