package ua.com.alternatiview.parkeonservice;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
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

    private static RadioGroup radio_group;
    //private static RadioButton radioButton;


    //String res;
    RadioButton rbAllDevicesSelect, rbActivDevicesSelect, rbNonActiveDeviceSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        btnShowOnMap = (Button) findViewById(R.id.btnShowOnMap);
        btnViewDetailed = (Button) findViewById(R.id.btnViewDetailed);
        btnAdvancedSearch = (Button) findViewById(R.id.btnAdvancedSearch);
        etSearchText = (EditText) findViewById(R.id.etSearchDeviceName);
        context = getApplicationContext();
        onClickListenerButGoMap();
    }

    //Обработка события нажатия кнопки ViewDetailed
    public void onVeiwDetailed(View view) {
        DB_connect con = new DB_connect();
        try {
            machineList = DB_connect.GetAllDevices();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, String.valueOf(machineList.size()), Toast.LENGTH_LONG).show();
    }

    //Обработка кнопки Go Map для показа всех устройств
    public void onClickListenerButGoMap() {
        radio_group = (RadioGroup)findViewById(R.id.rgSelector);
        btnShowOnMap = (Button)findViewById(R.id.btnShowOnMap);
        btnShowOnMap.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //Magic workaround to get statements for Switch
                        int selected_filter = radio_group.getCheckedRadioButtonId();
                        View rbt = radio_group.findViewById(selected_filter);
                        int rbtID = radio_group.indexOfChild(rbt);
                        RadioButton btn = (RadioButton) radio_group.getChildAt(rbtID);
                        String selectedTxt = (String) btn.getText();
                        switch (selectedTxt){
                            case "Show all devices":
                                Toast.makeText(context,selectedTxt,Toast.LENGTH_SHORT).show();
                                break;
                            case "Show Activated devices":
                                Toast.makeText(context,selectedTxt,Toast.LENGTH_SHORT).show();
                                break;
                            case "Show non-Activated devices":
                                Toast.makeText(context,selectedTxt,Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
        );
    }


}
