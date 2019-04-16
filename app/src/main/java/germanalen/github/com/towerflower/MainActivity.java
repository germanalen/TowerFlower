package germanalen.github.com.towerflower;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import germanalen.github.com.towerflower.database.Tower;
import germanalen.github.com.towerflower.database.TowerViewModel;


public class MainActivity extends AppCompatActivity {

    private TowerViewModel mTowerViewModel;
    public static final String TAG = "TOWER_FLOWER";

    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    // An endpoint was found. We request a connection to it.
                    Nearby.getConnectionsClient(MainActivity.this)
                            .requestConnection("test", endpointId, connectionLifecycleCallback)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Advertiser found");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }

                @Override
                public void onEndpointLost(String endpointId) {
                    // A previously discovered endpoint has gone away.
                }
            };

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    Log.d(TAG, "connection initiated");
                    Nearby.getConnectionsClient(MainActivity.this).acceptConnection(endpointId, new PayloadCallback() {
                        @Override
                        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
                            byte[] bytes = payload.asBytes();
                            String dna = new String(bytes);
                            Log.d(TAG, dna);
                        }

                        @Override
                        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

                        }
                    });
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            // We're connected! Can now start sending and receiving data.

                            Log.d(TAG, "connected");
                            String dna = "zxcv";
                            Payload payload = Payload.fromBytes(dna.getBytes());
                            Nearby.getConnectionsClient(MainActivity.this).sendPayload(endpointId, payload);
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            // The connection was rejected by one or both sides.
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            // The connection broke before it was able to be accepted.
                            break;
                        default:
                            // Unknown status code
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    // We've been disconnected from this endpoint. No more data can be
                    // sent or received.
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Requesting permission
        /* omitted believing its not necessary
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

        }
        */
        setContentView(R.layout.activity_main);





//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
//
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//
//        final MyAdapter mAdapter = new MyAdapter();
//        recyclerView.setAdapter(mAdapter);
//
//
//        mTowerViewModel = ViewModelProviders.of(this).get(TowerViewModel.class);
//
//        mTowerViewModel.getUserTowers().observe(this, new Observer<List<Tower>>() {
//            @Override
//            public void onChanged(@Nullable final List<Tower> towers) {
//                // Update the cached copy of the words in the adapter.
//                mAdapter.setTowers(towers);
//            }
//        });


    }

    public void startAdvertising(View view) {
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build();
        Nearby.getConnectionsClient(this)
                .startAdvertising( // TODO IF WRONG THEN CHANGE SERVICE ID
                        "test", "1", connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Advertising started");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void startDiscovery(View view) {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build();
        Nearby.getConnectionsClient(this)
                .startDiscovery("1", endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Discovery started");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}
