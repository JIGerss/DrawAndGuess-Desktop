package Client;

public class User {
    private String userName;
    private String userId;


    public User(){
        userId = "00000000-0000-0000-0000-000000000000";
        userName = "Default";
    }

    public User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
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
