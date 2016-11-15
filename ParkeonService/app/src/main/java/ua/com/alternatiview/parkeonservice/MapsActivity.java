package ua.com.alternatiview.parkeonservice;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private LinkedList<Device> machinesList = new LinkedList<>();
    private final DB_connect con = new DB_connect();
    public static LatLng newPoint;
    private AlertDialog.Builder dialogAddDeviceFromStorage, dialogChangeStatus, dialogAddRemark;
    private int intStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        DB_connect con = new DB_connect();
        @SuppressLint("HardwareIds") String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        machinesList = con.GetTempDevices(androidID);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        @SuppressLint("HardwareIds") String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        LatLng startPoint = new LatLng(machinesList.get(0).latitude, machinesList.get(0).longitude);
        con.DropTempTable(androidID);
        for (int i = 0; i < machinesList.size(); i++) {
            LatLng point = new LatLng(machinesList.get(i).latitude, machinesList.get(i).longitude);
            Marker m;
            if (machinesList.get(i).status == 1) {
                m = googleMap.addMarker(new MarkerOptions().position(point).title(String.valueOf(machinesList.get(i).machineID)).snippet("Activated")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).draggable(true));
                m.setTag(0);
                m.showInfoWindow();
            } else {
                Boolean isDraggable = true;
                if (machinesList.get(i).status == 9) {
                    m = googleMap.addMarker(new MarkerOptions().position(point).title(String.valueOf(machinesList.get(i).machineID)).snippet("Defective")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true));
                    m.setTag(0);
                    m.setDraggable(isDraggable);
                    m.showInfoWindow();
                } else {
                    m = googleMap.addMarker(new MarkerOptions().position(point).title(String.valueOf(machinesList.get(i).machineID)).snippet("Not Activated"));
                    m.setTag(0);
                    m.setDraggable(isDraggable);
                    m.showInfoWindow();
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        float zoom = 15;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, zoom));
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnInfoWindowClickListener(this);
    }

    //Добавление устройства на карту
    @Override
    public void onMapLongClick(LatLng latLng) {
        MoveToField(latLng);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        ChangeLocation(marker);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        ChangeStatus(marker);
    }

    //Обработка события клик на инфоокно - изменение статуса девайса
    private void ChangeStatus(final Marker marker) {
        dialogChangeStatus = new AlertDialog.Builder(this);
        final String[] strStatus = {"NotActivated", "Activated","Move to storage", "Defective", "Delete"};
        dialogChangeStatus.setTitle("Choose status for device");
        dialogChangeStatus.setSingleChoiceItems(strStatus, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String status = strStatus[which];
                switch (status) {
                    case "Activated":
                        intStatus = 1;
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        marker.setSnippet("Activated");
                        break;
                    case "NotActivated":
                        intStatus = 0;
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        marker.setSnippet("Not Activated");
                        break;
                    case "Move to storage":
                        intStatus = 11;
                        break;
                    case "Defective":
                        intStatus = 9;
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        marker.setSnippet("Defective");
                        break;
                    case "Delete": {
                        intStatus = 10;
                        break;
                        //TODO Create method to delete device with additional confirmation
                    }
                }
            }
        });

        dialogChangeStatus.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (intStatus < 10) {
                    DB_connect con = new DB_connect();
                    con.UpdateStatus(String.valueOf(marker.getTitle()), intStatus);
                }
                if (intStatus==10) {
                    DeleteDevice(marker);
                }
                if (intStatus == 11) {
                    con.MoveToStock(marker.getTitle());
                }
            }
        });
        dialogChangeStatus.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //Output
        AlertDialog dialogStatusChange = dialogChangeStatus.create();
        dialogStatusChange.show();
    }

    // Событие перетаскивания маркера
    private void ChangeLocation(final Marker marker) {
        dialogChangeStatus = new AlertDialog.Builder(this);
        dialogChangeStatus.setTitle("Confirm location changes");
        dialogChangeStatus.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DB_connect con = new DB_connect();
                con.UpdateLocation(String.copyValueOf(marker.getTitle().toCharArray()), marker.getPosition().latitude, marker.getPosition().longitude);
            }
        });
        dialogChangeStatus.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //Output
        AlertDialog dialogStatusChange = dialogChangeStatus.create();
        dialogStatusChange.show();
    }

    private void DeleteDevice(final Marker marker) {
        con.DeleteDevice(String.valueOf(marker.getTitle()));
    }

    private void MoveToField(final LatLng latLng){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_device_create_choise);
        dialog.setTitle("Select...");
        Button btnSelectFromStorage = (Button)dialog.findViewById(R.id.btnFromStorage);
        dialog.show();
        btnSelectFromStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final Dialog popup = new Dialog(MapsActivity.this);
                popup.setContentView(R.layout.device_from_database);
                popup.setTitle("Devices in the storage");
                final Spinner spinner = (Spinner) popup.findViewById(R.id.spinner1);
                LinkedList<String> list;
                list = con.GetDevicesFromStorage();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MapsActivity.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Button btnPlaceOnMap = (Button)popup.findViewById(R.id.btnDeviceSelected);
                btnPlaceOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        con.MoveToOnField(spinner.getSelectedItem().toString(),latLng.longitude, latLng.latitude);
                        popup.dismiss();
                    }
                });
                popup.show();
            }
        });
        Button btnCreateNew = (Button)dialog.findViewById(R.id.btnCreateNew);
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final Dialog popup = new Dialog(MapsActivity.this);
                popup.setContentView(R.layout.popup__confirm_new_device);
                final EditText editText = (EditText)popup.findViewById(R.id.etMachineName);
                Button button = (Button)popup.findViewById(R.id.btnOk);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                        Device machine = new Device(editText.getText().toString(),latLng.longitude, latLng.latitude);
                        con.InsertNewDevice(machine);

                    }
                });
                popup.show();
            }

        });
    }
}
