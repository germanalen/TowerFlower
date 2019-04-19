package germanalen.github.com.towerflower;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import germanalen.github.com.towerflower.database.Tower;
import germanalen.github.com.towerflower.graphics.MyGLRenderer;
import germanalen.github.com.towerflower.graphics.MyGLSurfaceView;
import germanalen.github.com.towerflower.graphics.TowerDrawer;

public class TowerEditorActivity extends AppCompatActivity {
    private Tower tower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_editor);


        tower = getIntent().getParcelableExtra("tower");
        MyGLSurfaceView glSurfaceView = findViewById(R.id.glSurfaceView);
        MyGLRenderer renderer = glSurfaceView.getRenderer();
        final TowerDrawer towerDrawer = renderer.getTowerDrawer();

        double[] dna = tower.decodeDna();
        towerDrawer.setDna(dna);


        LinearLayout layout = findViewById(R.id.seekBars);
        for (int i = 0; i < TowerDrawer.DNA_SIZE; ++i) {
            SeekBar seekBar = new SeekBar(this);
            seekBar.setProgress((int)(dna[i] * 100));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    100);
            seekBar.setLayoutParams(layoutParams);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    towerDrawer.setDna(getDnaFromUi());
                    towerDrawer.generateMesh();
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            layout.addView(seekBar);
        }
    }


    public void save(View v) {
        Log.d(MainActivity.TAG, "qwerqwerfwq34fq3");
        tower.encodeDna(getDnaFromUi());

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("towersx");
        Log.d(MainActivity.TAG, database.getKey());
        Log.d(MainActivity.TAG, tower.getId());
        Log.d(MainActivity.TAG, tower.toString());
        database.child(tower.getId()).setValue(tower);
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();




    }

    private double[] getDnaFromUi() {
        double[] dna = new double[TowerDrawer.DNA_SIZE];

        LinearLayout seekBars = findViewById(R.id.seekBars);
        for (int i = 0; i < seekBars.getChildCount(); ++i) {
            SeekBar seekBar = (SeekBar) seekBars.getChildAt(i);
            dna[i] = seekBar.getProgress() / 100.0;
        }

        return dna;
    }
}
