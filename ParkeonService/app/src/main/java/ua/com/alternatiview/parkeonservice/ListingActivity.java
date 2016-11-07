package ua.com.alternatiview.parkeonservice;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.LinkedList;

public class ListingActivity extends AppCompatActivity {


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         LinkedList<Device> machineList = new LinkedList<>();
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_listing);
         DB_connect con = new DB_connect();
         TextView textView = (TextView)findViewById(R.id.txtOutputString);

         String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
         machineList = con.GetTempDevices(androidID);
         textView.append(String.valueOf(machineList.size()));
         //for (int i = 0; i<machineList.size(); i++){
         //   textView.append("Latitude: "+machineList.get(i).latitude+'\n');
         //}
        con.DropTempTable(androidID);
     }
}



