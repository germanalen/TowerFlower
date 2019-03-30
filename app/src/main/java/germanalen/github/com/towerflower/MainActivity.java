package germanalen.github.com.towerflower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.GridLayoutManager;

import germanalen.github.com.towerflower.database.TowerViewModel;


public class MainActivity extends AppCompatActivity {

    private TowerViewModel mTowerViewModel;
    public static final String TAG = "TOWER_FLOWER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        MyAdapter mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);

                /*

                RecyclerView recyclerView = findViewById(R.id.recyclerview);
                final TowerListAdapter adapter = new TowerListAdapter(this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));



                mTowerViewModel = ViewModelProviders.of(this).get(TowerViewModel.class);

                mTowerViewModel.getAllTowers().observe(this, new Observer<List<Tower>>() {
                    @Override
                    public void onChanged(@Nullable final List<Tower> towers) {
                        // Update the cached copy of the words in the adapter.
                        adapter.setTowers(towers);
                    }
                });
                 */

    }

}
