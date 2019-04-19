package germanalen.github.com.towerflower;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import germanalen.github.com.towerflower.database.Tower;

public class UserData {
    private static UserData ourInstance;
    private String username;
    private ArrayList<Tower> userTowers = new ArrayList<>();
    public static UserData getInstance() {
        if (ourInstance == null) {
            ourInstance = new UserData();
        }

        return ourInstance;
    }

    private UserData() {
        Log.d(MainActivity.TAG, "new userdata");
    }

    public static void setUsername(String username) {
        getInstance().username = username;
    }

    public static String getUsername() {
        return getInstance().username;
    }

    public static void setUserTowers(ArrayList<Tower> userTowers) {
        getInstance().userTowers = userTowers;
    }

    public static ArrayList<Tower> getUserTowers() {
        return getInstance().userTowers;
    }

    public static void addTower(Tower tower) {
        getInstance().userTowers.add(tower);
    }

    public static void clearData() {
        getInstance().username = "";
        getInstance().userTowers.clear();
    }
}
