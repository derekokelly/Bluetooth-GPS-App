package ie.nuigalway.dokelly.bluetoothgpsapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BluetoothActivity extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int DISCOVERY_REQUEST = 1;
    private BluetoothAdapter btAdapter;
    private ArrayList deviceList = new ArrayList(); // arraylist to hold devices in
    private DatabaseReference mDatabase; // reference to firebase database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Bluetooth Device List"); // give page a title in appbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // setup methods
        setupBluetooth();
        findDevices();
        showList();

        // instantiate firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        // add listener to db
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                Iterable<DataSnapshot> devices = ds.child("devices").getChildren();
                for (DataSnapshot dataSnapshots : devices) {
                    Object data = dataSnapshots.getValue();
                    boolean containsBTData = deviceList.contains(data);
                    if (!containsBTData) {
                        deviceList.add(data);
                    }
                }
                showList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // Detects if device has bluetooth, and in case where it is turned off, prompts user to turn it on
    public void setupBluetooth() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            //Device doesn't support Bluetooth
        }
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);  //Pop-up appears on screen for user to enable bluetooth
        }
    }

    // when request code is the discovery code, call find devices
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DISCOVERY_REQUEST) {
            findDevices();
        }
    }

    // method to discover BT devices
    private void findDevices() {
        if (btAdapter.startDiscovery()) {
            registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    // broadcast receiver to push device name & address to firebase
    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceInfo = remoteDeviceName + ", " + remoteDevice;
            boolean containsDevice = deviceList.contains(deviceInfo);
            if (!containsDevice) {
                //deviceList.add(deviceInfo);
                mDatabase.child("devices").push().setValue(deviceInfo);
            }
            showList();
        }
    };

    // method to display list view of BT devices
    private void showList() {
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
        ListView listView = findViewById(R.id.deviceList);
        listView.setAdapter(adapter);
    }

}
