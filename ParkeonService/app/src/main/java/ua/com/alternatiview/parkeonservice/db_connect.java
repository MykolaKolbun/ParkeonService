package ua.com.alternatiview.parkeonservice;

import android.widget.Toast;

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

    //Метод взять данные определенного девайса из базы
    public Device GetDevice(String machineName) {
        BufferedReader stringToReciev;
        String result = "";

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
                    if (query_result > 0) {
                        for (int i = 0; i < device.length(); i++) {
                            String machineID = device.getJSONObject(i).getString("Name");
                            Double longitude = device.getJSONObject(i).getDouble("Longitude");
                            Double latitude = device.getJSONObject(i).getDouble("Longitude");
                            Boolean status;
                            int tempStatus = device.getJSONObject(i).getInt("Status");
                            if (tempStatus > 0) {
                                status = true;
                            } else {
                                status = false;
                            }
                            machine = new Device(machineID, longitude, latitude, status);
                        }
                    } else {
                        //TODO Show - jsonObj.getString("message")
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Show error "Error parsing JSON data."
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
        return machine;
    }

    // Метод забрать все девайсы из базы
    public LinkedList<Device> GetAllDevices() {
        LinkedList<Device> devicesList = new LinkedList<>();
        BufferedReader stringToReciev;
        String link = "http://parkeon.alternatiview.com.ua/get_all_devices.php?";
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            stringToReciev = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReciev.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray device = jsonObject.getJSONArray("Device");
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0) {
                        for (int i = 0; i < device.length(); i++) {
                            String machineID = device.getJSONObject(i).getString("Name");
                            Double longitude = device.getJSONObject(i).getDouble("Longitude");
                            Double latitude = device.getJSONObject(i).getDouble("Longitude");
                            Boolean status;
                            int tempStatus = device.getJSONObject(i).getInt("Status");
                            if (tempStatus > 0) {
                                status = true;
                            } else {
                                status = false;
                            }
                            devicesList.add(new Device(machineID, longitude, latitude, status));
                        }
                    } else {
                        //TODO Check if it's working.
                        String res = jsonObject.getString("message");
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //TODO Check if it's working
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
        return devicesList;
    }

    // Создание временной таблиці в БД
    public void CreateTempTable(String userID){
        BufferedReader stringToReciev;

        try {
            String link = "http://parkeon.alternatiview.com.ua/create_temp_table.php?tableName="+URLEncoder.encode(userID, "UTF-8");
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            stringToReciev = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = stringToReciev.readLine();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray device = jsonObject.getJSONArray("Device");
                    int query_result = jsonObject.getInt("success");
                    if (query_result > 0){

                    }
                    else{
                        String res = jsonObject.getString("message");;
                        Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    String res = "Error parsing JSON data.";
                    Toast.makeText(MainActivity.context, res, Toast.LENGTH_SHORT).show();
                }

            }
        }catch (Exception e){
            Toast.makeText(MainActivity.context, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }
    }
}
