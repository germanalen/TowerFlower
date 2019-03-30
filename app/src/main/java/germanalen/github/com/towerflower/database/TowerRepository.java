package germanalen.github.com.towerflower.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TowerRepository {
    private TowerDao mTowerDao;
    private LiveData<List<Tower>> mAllTowers;

    TowerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTowerDao = db.towerDao();
        mAllTowers = mTowerDao.getAllTowers();
    }

    LiveData<List<Tower>> getAllTowers() {
        return mAllTowers;
    }

    public void insert(Tower tower) {
        new insertAsyncTask(mTowerDao).execute(tower);
    }

    public void update(Tower tower) {
        new updateAsyncTask(mTowerDao).execute(tower);
    }

    public void delete(Tower tower) {
        new deleteAsyncTask(mTowerDao).execute(tower);
    }

    private static class insertAsyncTask extends AsyncTask<Tower, Void, Void> {

        private TowerDao mAsyncTaskDao;

        insertAsyncTask(TowerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Tower... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Tower, Void, Void> {

        private TowerDao mAsyncTaskDao;

        updateAsyncTask(TowerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Tower... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Tower, Void, Void> {

        private TowerDao mAsyncTaskDao;

        deleteAsyncTask(TowerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Tower... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
