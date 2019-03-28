package germanalen.github.com.towerflower;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tower_table")
public class Tower {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "creator_name")
    public String creatorName;

    @ColumnInfo(name = "dna")
    public double dna;

    public Tower(int id, String creatorName, double dna) {
        this.id = id;
        this.creatorName = creatorName;
        this.dna = dna;
    }
}
