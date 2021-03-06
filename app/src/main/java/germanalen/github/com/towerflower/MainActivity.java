package germanalen.github.com.towerflower;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import germanalen.github.com.towerflower.database.Tower;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TOWER_FLOWER";
    private ArrayList<Tower> towerList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        //
        // Firebase part
        //

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("towersx");

//        Tower testTower = new Tower("firstOne", "");
//        testTower.encodeDna(new double[]{0.9,0.1,0.1,0.2,0.4});
//        database.push().setValue(testTower);
//
//        testTower = new Tower("firstOneq", "");
//        testTower.encodeDna(new double[]{0.1,0.9,0.9,0.8,0.6});
//        database.push().setValue(testTower);

        // Read from database
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                towerList.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Tower tower = childDataSnapshot.getValue(Tower.class);
                    if (tower.creatorName.equals(UserData.getUsername())) {
                        towerList.add(tower);
                    }
                }

                if (towerList.isEmpty()) {
                    // Notify user that he is new and offer to build his first tower via AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Want to create one?")
                            .setTitle("You have no towers");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Tower tower= new Tower(UserData.getUsername(), "");
                            tower.encodeDna(new double[]{0.9,0.1,0.1,0.2,0.4});
                            DatabaseReference myref = database.push();
                            tower.setId(myref.getKey());
                            myref.setValue(tower);

                            Intent intent = new Intent(MainActivity.this, TowerEditorActivity.class);
                            intent.putExtra("tower", tower);
                            startActivity(intent);
                            UserData.setUserTowers(towerList);
                            setContentView(R.layout.activity_main);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                } else {

                    UserData.setUserTowers(towerList);
                    setContentView(R.layout.activity_main);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });


    }

    public void startTowersOverviewActivity(View view) {
        Intent intent = new Intent(this, TowersOverviewActivity.class);
        intent.putExtra("condition", 0);
        startActivity(intent);
    }
    public void startSearchCoupleActivity(View view) {
        Intent intent = new Intent(this, TowersOverviewActivity.class);
        intent.putExtra("condition", 1);
        startActivity(intent);
    }
}
