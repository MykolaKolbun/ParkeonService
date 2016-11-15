package ua.com.alternatiview.parkeonservice;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sorrow on 08.11.2016.
 */
public class ConfirmNewDevice extends Activity {
    private EditText etName;
    private LatLng point;
    private Button Ok;
    //Button Cancel;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup__confirm_new_device);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.8),(int) (height*.4));
        etName = (EditText) findViewById(R.id.etMachineName);

        onClickOK();
    }
    // ЩОбработка события нажатия кнопки ОК
    private void onClickOK(){
        Ok = (Button)findViewById(R.id.btnOk);
        Ok.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(!String.valueOf(etName.getText()).equals("")){
                            point = MapsActivity.newPoint;
                            Device machine = new Device(String.valueOf(etName.getText()), point.longitude, point.latitude, 2);
                            DB_connect con = new DB_connect();
                            try {
                                con.InsertNewDevice(machine);
                                Ok.setVisibility(View.GONE);
                            } catch (Exception e){
                                //TODO Show exception
                            }
                        }
                    }
                }
        );

    }

}
