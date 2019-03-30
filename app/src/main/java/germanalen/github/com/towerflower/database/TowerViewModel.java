package germanalen.github.com.towerflower.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class TowerViewModel extends AndroidViewModel {
    private TowerRepository mRepository;

    public TowerViewModel(Application application) {
        super(application);
        mRepository = new TowerRepository(application);
    }

    public LiveData<List<Tower>> getAllTowers() {
        return mRepository.getAllTowers();
    }

    public LiveData<List<Tower>> getUserTowers() {
        return mRepository.getUserTowers();
    }

    public void insert(Tower tower) {
        mRepository.insert(tower);
    }

    public void update(Tower tower) {
        mRepository.update(tower);
    }

    public void delete(Tower tower) {
        mRepository.delete(tower);
    }
}
