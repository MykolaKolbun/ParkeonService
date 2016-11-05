package ua.com.alternatiview.parkeonservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ListingActivity extends AppCompatActivity {

    DB_connect r= new DB_connect();
    public TextView outText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //outText = (TextView)findViewById(R.id.txtOutputString);
        //DB_connect db_connect = new DB_connect();
        //db_connect.GetDevice(MainActivity.etSearchText.toString());
        //outText.setText(r.name);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

    }
}
