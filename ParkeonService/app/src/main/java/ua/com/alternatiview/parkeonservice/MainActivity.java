package ua.com.alternatiview.parkeonservice;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
    Button btnShowOnMap, btnAdvancedSearch, btnViewDetailed;
    static EditText etSearchText;
    Device machine;
    private static RadioGroup radio_group;
    DB_connect con = new DB_connect();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        etSearchText = (EditText) findViewById(R.id.etSearchDeviceName);
        context = getApplicationContext();
        onClickListenerButGoMap();
        onVeiwDetailed();
        onClickListenerButViewDetailed();
    }

    //Обработка события нажатия кнопки ViewDetailed
    private void onVeiwDetailed() {
        btnAdvancedSearch = (Button) findViewById(R.id.btnAdvancedSearch);

        btnAdvancedSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(MainActivity.context, MapsActivity.class);
                        String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        con.CreateTempTable(androidID);
                        try {
                            machine = con.GetDevice(etSearchText.getText().toString());
                            if (machine.longitude != 0) {
                                con.InsertToTempTable(androidID, machine.machineID);
                                MainActivity.this.startActivity(myIntent);
                            } else {
                                Toast.makeText(context, "Device not found in table", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
        );
    }

    //Обработка кнопки Go Map для показа всех устройств
    private void onClickListenerButGoMap() {
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
                        Intent myIntent = new Intent(MainActivity.context, MapsActivity.class);
                        switch (selectedTxt) {
                            case "Show all devices":
                                machineList = con.GetAllDevices();
                                con.InsertToTempTable(androidID);
                                MainActivity.this.startActivity(myIntent);
                                break;
                            case "Show Activated devices":
                                machineList = con.GetSelectedDevices(1);
                                con.InsertToTempTable(androidID, 1);
                                MainActivity.this.startActivity(myIntent);
                                break;
                            case "Show non-Activated devices":
                                machineList = con.GetSelectedDevices(0);
                                con.InsertToTempTable(androidID, 0);
                                MainActivity.this.startActivity(myIntent);
                                break;
                        }
                    }
                }
        );
    }

    private void onClickListenerButViewDetailed() {
        btnViewDetailed = (Button) findViewById(R.id.btnViewDetailed);

        btnViewDetailed.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(MainActivity.context, ListingActivity.class);
                        String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        con.CreateTempTable(androidID);
                        int selected_filter = radio_group.getCheckedRadioButtonId();
                        View rbt = radio_group.findViewById(selected_filter);
                        int rbtID = radio_group.indexOfChild(rbt);
                        RadioButton btn = (RadioButton) radio_group.getChildAt(rbtID);
                        String selectedTxt = (String) btn.getText();
                        switch (selectedTxt) {
                            case "Show all devices":
                                machineList = con.GetAllDevices();
                                con.InsertToTempTable(androidID);
                                MainActivity.this.startActivity(myIntent);
                                break;
                            case "Show Activated devices":
                                machineList = con.GetSelectedDevices(1);
                                con.InsertToTempTable(androidID, 1);
                                MainActivity.this.startActivity(myIntent);
                                break;
                            case "Show non-Activated devices":
                                machineList = con.GetSelectedDevices(0);
                                con.InsertToTempTable(androidID, 0);
                                MainActivity.this.startActivity(myIntent);
                                break;
                        }
                    }
                }
        );
    }

}
