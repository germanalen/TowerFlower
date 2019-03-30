package germanalen.github.com.towerflower.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TowerDao {
    @Query("SELECT * FROM tower_table")
    LiveData<List<Tower>> getAllTowers();

    @Query("DELETE FROM tower_table")
    void deleteAll();

    @Update
    void update(Tower tower);

    @Insert
    void insert(Tower tower);

    @Delete
    void delete(Tower tower);
}
