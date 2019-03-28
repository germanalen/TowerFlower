package germanalen.github.com.towerflower;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TowerViewModel mTowerViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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



    }

}
