package ua.com.alternatiview.parkeonservice;

/**
 * Created by Sorrow on 02.11.2016.
 */

public class Device {
    public String machineID;
    public double longitude;
    public double latitude;
    public boolean status;
    public Device (String machineID){
        this.machineID = machineID;
    }
    public Device (String machineID, double latitude, double longitude ){
        this.machineID = machineID;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public Device (String machineID, double latitude, double longitude, boolean status ){
        this.machineID = machineID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
    }
}
