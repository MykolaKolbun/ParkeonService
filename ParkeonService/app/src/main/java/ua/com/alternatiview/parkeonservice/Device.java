package ua.com.alternatiview.parkeonservice;

/**
 * Created by Sorrow on 02.11.2016.
 */

public class Device {
    public String machineID;
    public double longitude;
    public double latitude;
    public boolean status;

    public Device(String machineID) {
        this.machineID = machineID;
    }

    public Device(String machineID, double latitude, double longitude) {
        this.machineID = machineID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = false;
    }

    public Device(String machineID, double latitude, double longitude, boolean status) {
        this.machineID = machineID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
    }

    public String[] PrintDevice(Device device) {
        String[] printResult = new String[4];
        printResult[0] = device.machineID;
        printResult[1] = Double.toString(device.longitude);
        printResult[2] = Double.toString(device.latitude);
        printResult[3] = Boolean.toString(device.status);

        return printResult;
    }
}
