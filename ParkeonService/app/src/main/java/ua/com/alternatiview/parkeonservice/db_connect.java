package ua.com.alternatiview.parkeonservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Sorrow on 02.11.2016.
 */



public class DB_connect {

    public void GetDevice(String machineName){
        BufferedReader stringToReciev;
        String result;
        try {
            String link = "http://parkeon.alternatiview.com.ua/get_device.php?name="+ URLEncoder.encode(machineName,"UTF-8");
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            stringToReciev = new BufferedReader(new InputStreamReader(con.getInputStream()));
            result = stringToReciev.readLine();
            if (result != null){
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Boolean query_result = jsonObject.getBoolean("success");
                    if (query_result){
                        //TODO Fill out machine from JSON

                    }
                    else {
                        //TODO Show - jsonObj.getString("message")
                    }
                }catch (JSONException e){
                    //TODO Show error "Error parsing JSON data."
                }
            }



        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
