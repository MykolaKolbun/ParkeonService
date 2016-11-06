package ua.com.alternatiview.parkeonservice;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    static Context context;
    LinkedList<Device> machineList = new LinkedList<>();
    Button btnShowOnMap, btnViewDetailed, btnAdvancedSearch;
    static EditText etSearchText;
    Device machine;
    String res;
    //String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    private static RadioGroup radio_group;
    DB_connect con = new DB_connect();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //btnShowOnMap = (Button) findViewById(R.id.btnShowOnMap);
        //btnViewDetailed = (Button) findViewById(R.id.btnViewDetailed);
        //btnAdvancedSearch = (Button) findViewById(R.id.btnAdvancedSearch);
        etSearchText = (EditText) findViewById(R.id.etSearchDeviceName);
        context = getApplicationContext();
        onClickListenerButGoMap();
        onVeiwDetailed();


    }

    //Обработка события нажатия кнопки ViewDetailed
    public void onVeiwDetailed() {
        //DB_connect con = new DB_connect();
        btnAdvancedSearch = (Button)findViewById(R.id.btnAdvancedSearch);
        btnAdvancedSearch.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //DB_connect con = new DB_connect();
                        try {
                            //Toast.makeText(context, etSearchText.getText().toString(), Toast.LENGTH_LONG).show();
                            machine = con.GetDevice(etSearchText.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(context, String.valueOf(machine.latitude), Toast.LENGTH_LONG).show();
                        //Intent listingLayout = new Intent(MainActivity.context, ListingActivity.class);
                        //startActivity(listingLayout);
                    }

                }
        );
    }

    //Обработка кнопки Go Map для показа всех устройств
    public void onClickListenerButGoMap() {

        radio_group = (RadioGroup) findViewById(R.id.rgSelector);
        btnShowOnMap = (Button) findViewById(R.id.btnShowOnMap);

        btnShowOnMap.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Magic workaround to get statements for Switch
                        String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        con.CreateTempTable(androidID);
                        int selected_filter = radio_group.getCheckedRadioButtonId();
                        View rbt = radio_group.findViewById(selected_filter);
                        int rbtID = radio_group.indexOfChild(rbt);
                        RadioButton btn = (RadioButton) radio_group.getChildAt(rbtID);
                        String selectedTxt = (String) btn.getText();
                        LinkedList<Device> machinelist = new LinkedList<>();
                        switch (selectedTxt) {
                            case "Show all devices":
                                machinelist = con.GetAllDevices();
                                con.InsertToTempTable(androidID, machinelist);
                                break;
                            case "Show Activated devices":
                                machinelist = con.GetSelectedDevices(1);
                                con.InsertToTempTable(androidID, machinelist);
                                break;
                            case "Show non-Activated devices":
                                machinelist = con.GetSelectedDevices(0);
                                con.InsertToTempTable(androidID, machinelist);
                                break;
                        }
                    }
                }
        );
    }


}
