package germanalen.github.com.towerflower.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.annotations.NotNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import germanalen.github.com.towerflower.MainActivity;
import germanalen.github.com.towerflower.graphics.TowerDrawer;

public class Tower implements Parcelable {

    public String id;
    public String creatorName;

    public String dnaJson;

    public Tower() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Tower(@NotNull String creatorName, String dnaJson) {
        this.creatorName = creatorName;
        this.dnaJson = dnaJson;
    }

    @Override
    public String toString() {
        return "Creator: " + creatorName + "; dna: " + dnaJson;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public double[] decodeDna() {
        double[] dna = new double[TowerDrawer.DNA_SIZE];
        try {
            JSONArray jsonArray = new JSONArray(dnaJson);

            if (TowerDrawer.DNA_SIZE != jsonArray.length())
                throw new RuntimeException("dna_json has wrong length");

            for (int i = 0; i < jsonArray.length(); ++i) {
                dna[i] = jsonArray.getDouble(i);
            }
        } catch (JSONException ex) {
            Log.e(MainActivity.TAG, "Error decoding dna_json");
            dna = new double[TowerDrawer.DNA_SIZE]; // 0 dna tower makes it easier to notice this error
        }
        return dna;
    }

    public void encodeDna(double[] dna) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (double v : dna) {
                jsonArray.put(v);
            }
        } catch (JSONException ex) {
            Log.e(MainActivity.TAG, "Error encoding dna_json");
        }
        dnaJson = jsonArray.toString();
    }



    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(creatorName);
        out.writeString(dnaJson);
    }

    public static final Parcelable.Creator<Tower> CREATOR
            = new Parcelable.Creator<Tower>() {
        public Tower createFromParcel(Parcel in) {
            return new Tower(in);
        }

        public Tower[] newArray(int size) {
            return new Tower[size];
        }
    };

    private Tower(Parcel in) {
        id = in.readString();
        creatorName = in.readString();
        dnaJson = in.readString();
    }
}
