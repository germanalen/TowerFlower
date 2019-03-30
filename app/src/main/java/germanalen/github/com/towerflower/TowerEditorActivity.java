package germanalen.github.com.towerflower;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import germanalen.github.com.towerflower.graphics.MyGLRenderer;
import germanalen.github.com.towerflower.graphics.MyGLSurfaceView;
import germanalen.github.com.towerflower.graphics.TowerDrawer;

public class TowerEditorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_editor);


        double[] dna = getIntent().getDoubleArrayExtra("dna");
        MyGLSurfaceView glSurfaceView = findViewById(R.id.glSurfaceView);
        MyGLRenderer renderer = glSurfaceView.getRenderer();
        TowerDrawer towerDrawer = renderer.getTowerDrawer();
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
                    updateDna();
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            layout.addView(seekBar);
        }
    }

    private void updateDna() {
        double[] dna = new double[TowerDrawer.DNA_SIZE];

        LinearLayout seekBars = findViewById(R.id.seekBars);
        for (int i = 0; i < seekBars.getChildCount(); ++i) {
            SeekBar seekBar = (SeekBar) seekBars.getChildAt(i);
            dna[i] = seekBar.getProgress() / 100.0;
        }



        MyGLSurfaceView glSurfaceView = findViewById(R.id.glSurfaceView);
        MyGLRenderer renderer = glSurfaceView.getRenderer();
        TowerDrawer towerDrawer = renderer.getTowerDrawer();
        towerDrawer.setDna(dna);
        towerDrawer.generateMesh();
    }
}
