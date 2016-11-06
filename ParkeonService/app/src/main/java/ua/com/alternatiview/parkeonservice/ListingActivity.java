package ua.com.alternatiview.parkeonservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

 public class ListingActivity extends AppCompatActivity {


     @Override
     protected void onCreate(Bundle savedInstanceState) {

         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_listing);

         TextView textView = (TextView)findViewById(R.id.txtOutputString);

         //for (int i = 0; i<machineList.size(); i++){
         textView.setText("Hello from Main Activity");
         //textView.append("Latitude: "+machine.latitude);
         //}

     }
}



