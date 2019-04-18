package germanalen.github.com.towerflower;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import germanalen.github.com.towerflower.database.Tower;

public class SearchCoupleActivity extends AppCompatActivity {
    public static final String TAG = "TOWER_FLOWER";
    private Tower selectedTower;
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    // An endpoint was found. We request a connection to it.
                    Nearby.getConnectionsClient(SearchCoupleActivity.this)
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
                    Nearby.getConnectionsClient(SearchCoupleActivity.this).acceptConnection(endpointId, new PayloadCallback() {
                        @Override
                        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
                            byte[] bytes = payload.asBytes();
                            String arrivedDna = new String(bytes);
                            Tower tempTowerToGetDecodedDna = new Tower(0, "java", arrivedDna);
                            Tower babyTower = new Tower(selectedTower.id * 2, UserData.getUsername(), "");
                            double[] babyDna = new double[5];
                            for (int i = 0; i < 5; ++i) {
                                babyDna[i] = (selectedTower.decodeDna()[i] + tempTowerToGetDecodedDna.decodeDna()[i]) / 2;
                            }
                            babyTower.encodeDna(babyDna);

                            // Pushing baby to database
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                            database.child("towers").child(String.valueOf(babyTower.id)).setValue(babyTower);

                            Log.d(TAG, "Arrived dna: " + arrivedDna);
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
                            String dna = selectedTower.dnaJson;
                            Payload payload = Payload.fromBytes(dna.getBytes());
                            Nearby.getConnectionsClient(SearchCoupleActivity.this).sendPayload(endpointId, payload);
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

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        setContentView(R.layout.activity_search_couple);
        selectedTower = getIntent().getParcelableExtra("tower");
        startDiscovery();
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

    public void startDiscovery() {
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
                        Log.d(TAG, "Discovery failed");
                    }
                });
    }


}
