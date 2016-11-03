package ua.com.alternatiview.parkeonservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;


/**
 * Created by Sorrow on 02.11.2016.
 */


public class DB_connect {

    public String name;
    Double longitude, latitude;
    Boolean status;


    //Метод взять данные определенного девайса из базы
    public Device GetDevice(String machineName) {
        BufferedReader stringToReciev;
        String result="";
        Device machine = new Device(machineName);

        try {
            String link = "http://parkeon.alternatiview.com.ua/get_device.php?name=" + URLEncoder.encode(machineName, "UTF-8");
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            stringToReciev = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = stringToReciev.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray device = jsonObject.getJSONArray("Device");

                    int query_result = jsonObject.getInt("success");
                    if (query_result>0) {
                        //TODO Fill out machine from JSON
                        for (int i =0; i<device.length();i++) {
                            machine.machineID = device.getJSONObject(i).getString("Name");
                            //longitude = device.getJSONObject(i).getDouble("Longitude");
                            //longitude = 40.123456;
                            machine.longitude = device.getJSONObject(i).getDouble("Longitude");
                            //latitude = device.getJSONObject(i).getDouble("Latitude");
                            machine.latitude = device.getJSONObject(i).getDouble("Longitude");
                            //status = device.getJSONObject(i).getBoolean("Status");
                            machine.status = device.getJSONObject(i).getBoolean("Status");
                            //machine = new Device(name,latitude,longitude,status);
                        }

                    } else {
                        //TODO Show - jsonObj.getString("message")

                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return machine;
    }

    // Метод забрать все девайсы из базы
    public LinkedList<Device> GetAllDevices() {
        LinkedList<Device> tempDevicesList = new LinkedList<Device>();
        BufferedReader stringToReciev;
        String result;
        Device machine;
        String link = "http://parkeon.alternatiview.com.ua/get_all_devices.php?";



        return tempDevicesList;
    }
}
