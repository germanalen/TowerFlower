package germanalen.github.com.towerflower;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import germanalen.github.com.towerflower.database.Tower;

public class TowersOverviewActivity extends AppCompatActivity {
    public static final String TAG = "TOWER_FLOWER";
    private ArrayList<Tower> mAllTowers = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towers_oveview);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Bundle bundle = getIntent().getExtras();
        int condition = bundle.getInt("condition");
        if (condition == 1) {
            getSupportActionBar().setTitle("Choose your tower");
        }

        final MyAdapter mAdapter = new MyAdapter(condition);
        recyclerView.setAdapter(mAdapter);

        mAllTowers.clear();
        mAllTowers = UserData.getUserTowers();
        mAdapter.setTowers(mAllTowers);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("towersx");
        databaseRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  mAllTowers.clear();
                  for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                      Tower tower = childDataSnapshot.getValue(Tower.class);
                      if (tower.creatorName.equals(UserData.getUsername())) {
                          mAllTowers.add(tower);
                      }
                  }
                  UserData.setUserTowers(mAllTowers);
                  mAdapter.setTowers(mAllTowers);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                    Tower tower = childDataSnapshot.getValue(Tower.class);
//                    if (tower.creatorName.equals(UserData.getUsername())) {
//                        mAllTowers.add(tower);
//                    }
//                }
//                if (mAllTowers.isEmpty()) {
//                    // Notify user that he is new and offer to build his first tower via AlertDialog
//                    AlertDialog.Builder builder = new AlertDialog.Builder(TowersOverviewActivity.this);
//                    builder.setMessage("Want to create one?")
//                            .setTitle("You have no towers");
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Intent intent = new Intent(TowersOverviewActivity.this, SearchCoupleActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            finish();
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.setCanceledOnTouchOutside(true);
//                    dialog.show();
//
//                } else {
//                    mAdapter.setTowers(mAllTowers);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
//            }
//        });

    }
}
