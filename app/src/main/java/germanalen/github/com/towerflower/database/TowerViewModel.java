package germanalen.github.com.towerflower.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class TowerViewModel extends AndroidViewModel {
    private TowerRepository mRepository;
    private LiveData<List<Tower>> mAllTowers;

    public TowerViewModel(Application application) {
        super(application);
        mRepository = new TowerRepository(application);
        mAllTowers = mRepository.getAllTowers();
    }

    LiveData<List<Tower>> getAllTowers() {
        return mAllTowers;
    }

    public void insert(Tower tower) {
        mRepository.insert(tower);
    }
}
