package ie.nuigalway.dokelly.bluetoothgpsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button bluetoothButton;
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothButton = findViewById(R.id.bluetoothDevicesButton);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBluetoothActivity();
            }
        });

        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });
    }

    public void openBluetoothActivity() {
        Intent bluetoothIntent = new Intent(this, BluetoothActivity.class);
        startActivity(bluetoothIntent);
    }

    public void openMapActivity() {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }
}
