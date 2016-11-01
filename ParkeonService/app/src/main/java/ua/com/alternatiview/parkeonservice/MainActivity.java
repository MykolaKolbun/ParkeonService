package ua.com.alternatiview.parkeonservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    Button btnShowOnMap, btnViewDetailed, btnAdvancedSearch;
    RadioButton rbAllDevicesSelect, rbActivDevicesSelect, rbNonActiveDeviceSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnShowOnMap = (Button) findViewById(R.id.btnShowOnMap);
        btnViewDetailed = (Button) findViewById(R.id.btnViewDetailed);
        btnAdvancedSearch = (Button) findViewById(R.id.btnAdvancedSearch);
    }


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
