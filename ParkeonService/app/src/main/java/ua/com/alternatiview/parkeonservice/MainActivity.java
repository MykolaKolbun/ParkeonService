package ua.com.alternatiview.parkeonservice;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    Context context;
    LinkedList<Device> machineList = new LinkedList<Device>();
    Button btnShowOnMap, btnViewDetailed, btnAdvancedSearch;
    static EditText etSearchText;
    Device machine;
    Double res;


    //String res;
    //RadioButton rbAllDevicesSelect, rbActivDevicesSelect, rbNonActiveDeviceSelect;

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
    }

    //Обработка события нажатия кнопки ViewDetailed
    public void onVeiwDetailed(View view){
        DB_connect con = new DB_connect();
        try{
            machine = con.GetDevice("10031141");
            //res = con.GetDevice("10031141");
        }catch (Exception e){
            Toast.makeText(context, e.toString(),Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, Double.toString(machine.latitude),
                Toast.LENGTH_LONG).show();;
    }
    //Обработка выбора типа фильтра для показа на карте
    public void onSelectToShow(View view) {
        boolean checkToShow = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbAllDevicesSelect:
                if (checkToShow) {
                    //TODO set to show all devices
                    break;
                }
            case R.id.rbActivDevicesSelect:
                if (checkToShow) {
                    //TODO set to show only activated devices
                    break;
                }
            case R.id.rbNonActiveDeviceSelect:
                if (checkToShow) {
                    //TODO set to show only not activated devices
                    break;
                }
        }
    }
}
