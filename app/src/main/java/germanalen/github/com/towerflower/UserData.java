package germanalen.github.com.towerflower;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import germanalen.github.com.towerflower.database.Tower;

public class UserData {
    private static final UserData ourInstance = new UserData();
    private String username;
    private ArrayList<Tower> userTowers = new ArrayList<>();
    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
    }

    public static void setUsername(String username) {
        ourInstance.username = username;
    }

    public static String getUsername() {
        return ourInstance.username;
    }

    public static void setUserTowers(ArrayList<Tower> userTowers) {
        ourInstance.userTowers = userTowers;
    }

    public static ArrayList<Tower> getUserTowers() {
        return ourInstance.userTowers;
    }

    public static void addTower(Tower tower) {
        ourInstance.userTowers.add(tower);
    }

    public static void clearData() {
        ourInstance.username = "";
        ourInstance.userTowers.clear();
    }
}
