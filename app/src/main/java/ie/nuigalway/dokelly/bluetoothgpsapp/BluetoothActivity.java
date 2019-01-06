package ie.nuigalway.dokelly.bluetoothgpsapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BluetoothActivity extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int DISCOVERY_REQUEST = 1;
    private BluetoothAdapter btAdapter;
    private ArrayList deviceList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        setupBluetooth();
        findDevices();
        showList();

//        int MINUTES = 20; // The delay in minutes
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() { // Function runs every MINUTES minutes.
//                // Run the code you want here
//                findDevices(); // If the function you wanted was static
//            }
//        }, 0, 1000 * 1 * MINUTES);
    }

    //Detects if device has bluetooth, and in case where it is turned off, prompts user to turn it on
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

    BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String prevStateExtra = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateExtra, -1);
            int prevState = intent.getIntExtra(prevStateExtra, -1);

            switch (state) {
                case (BluetoothAdapter.STATE_TURNING_ON):
                {
                    Log.i("BluetoothAdapter", "bluetooth turning on");
                    Toast.makeText(BluetoothActivity.this, "Bluetooth turning on", Toast.LENGTH_SHORT).show();
                    break;
                }
                case (BluetoothAdapter.STATE_ON):
                {
                    Log.i("BluetoothAdapter", "bluetooth on");
                    Toast.makeText(BluetoothActivity.this, "Bluetooth on", Toast.LENGTH_SHORT).show();
                    break;
                }
                case (BluetoothAdapter.STATE_TURNING_OFF):
                {
                    Log.i("BluetoothAdapter", "bluetooth turning off");
                    Toast.makeText(BluetoothActivity.this, "Bluetooth turning off", Toast.LENGTH_SHORT).show();
                    break;
                }
                case (BluetoothAdapter.STATE_OFF):
                {
                    Log.i("BluetoothAdapter", "bluetooth off");
                    Toast.makeText(BluetoothActivity.this, "Bluetooth off", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    public void enableDiscoverable(View view) {
        String scanModeChanged = BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
        String beDiscoverable = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;

        IntentFilter filter = new IntentFilter(scanModeChanged);
        registerReceiver(bluetoothState, filter);
        Intent discoverableIntent = new Intent(beDiscoverable);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, DISCOVERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DISCOVERY_REQUEST) {
            findDevices();
        }
    }

    private void findDevices() {
        if (btAdapter.startDiscovery()) {
            registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceInfo = remoteDeviceName + ", " + remoteDevice;
            boolean containsDevice = deviceList.contains(deviceInfo);
            if (!containsDevice) {
                deviceList.add(deviceInfo);
            }
            showList();
        }
    };

    private void showList() {
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);
        ListView listView = findViewById(R.id.deviceList);
        listView.setAdapter(adapter);
    }

}
