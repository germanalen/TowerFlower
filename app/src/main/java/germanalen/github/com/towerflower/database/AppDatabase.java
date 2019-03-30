package germanalen.github.com.towerflower.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Tower.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TowerDao towerDao();

    // Singleton
    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "tower_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TowerDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.towerDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Tower tower= new Tower(1, "firstOne", "");
            tower.encodeDna(new double[]{0.9,0.1,0.1,0.2,0.4});
            mDao.insert(tower);
            tower = new Tower(2, "firstOne", "");
            tower.encodeDna(new double[]{0.9,0.1,0.9,0.2,0.4});
            mDao.insert(tower);
            tower = new Tower(3, "secondOne", "");
            tower.encodeDna(new double[]{0.2,0.1,0.9,0.6,0.2});
            mDao.insert(tower);
            tower = new Tower(4, "secondOne", "");
            tower.encodeDna(new double[]{0.4,0.1,0.9,0.6,0.2});
            mDao.insert(tower);

            return null;
        }
    }
}
