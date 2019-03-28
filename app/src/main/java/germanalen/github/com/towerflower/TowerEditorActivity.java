package germanalen.github.com.towerflower;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class TowerEditorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_editor);


        LinearLayout layout = findViewById(R.id.seekBars);
        for (int i = 0; i < TowerDrawer.DNA_SIZE; ++i) {
            SeekBar seekBar = new SeekBar(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    100);
            seekBar.setLayoutParams(layoutParams);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    updateDna();
                }
            });

            layout.addView(seekBar);
        }
    }

    private void updateDna() {
        float[] dna = new float[TowerDrawer.DNA_SIZE];

        LinearLayout seekBars = findViewById(R.id.seekBars);
        for (int i = 0; i < seekBars.getChildCount(); ++i) {
            SeekBar seekBar = (SeekBar) seekBars.getChildAt(i);
            dna[i] = seekBar.getProgress() / 100f;
        }



        MyGLSurfaceView glSurfaceView = findViewById(R.id.glSurfaceView);
        MyGLRenderer renderer = glSurfaceView.getRenderer();
        TowerDrawer towerDrawer = renderer.getTowerDrawer();
        towerDrawer.generateMesh(dna);
    }
}
