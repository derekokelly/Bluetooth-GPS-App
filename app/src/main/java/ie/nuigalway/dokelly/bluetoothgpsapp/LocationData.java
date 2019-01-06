package ie.nuigalway.dokelly.bluetoothgpsapp;

import java.util.ArrayList;

public class LocationData {

    public double latitude;
    public double longitude;
    public ArrayList devices;

    private LocationData() {}

//    public LocationData(double latitude, double longitude) {
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }

    public LocationData(double latitude, double longitude, ArrayList list) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.devices = list;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getNumDevices() {
        return this.devices.size();
    }
}
