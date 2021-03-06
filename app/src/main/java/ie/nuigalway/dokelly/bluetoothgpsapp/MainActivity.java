package ie.nuigalway.dokelly.bluetoothgpsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button bluetoothButton; // two buttons on main page
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get BT button & add listener
        bluetoothButton = findViewById(R.id.bluetoothDevicesButton);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBluetoothActivity();
            }
        });

        // get map button & add listener
        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });

//        Intent service = new Intent(getApplicationContext(), GPSService.class);
//        startService(service);
    }

    // method to navigate to BT activity
    public void openBluetoothActivity() {
        Intent bluetoothIntent = new Intent(this, BluetoothActivity.class);
        startActivity(bluetoothIntent);
    }

    // method to navigate to map activity
    public void openMapActivity() {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }
}
