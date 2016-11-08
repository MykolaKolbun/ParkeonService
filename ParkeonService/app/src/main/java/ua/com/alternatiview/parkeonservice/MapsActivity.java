package ua.com.alternatiview.parkeonservice;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

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

    private GoogleMap mMap;
    LinkedList<Device> machinesList = new LinkedList<Device>() ;
    DB_connect con = new DB_connect();
    Boolean isDraggable = true;
    public static LatLng newPoint;
    private AlertDialog.Builder dialogConfirmLocation, dialogChangeStatus, dialogAddRemark;
    int intStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        DB_connect con = new DB_connect();
        String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        machinesList = con.GetTempDevices(androidID);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String androidID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        LatLng startPoint = new LatLng(machinesList.get(0).latitude, machinesList.get(0).longitude);
        con.DropTempTable(androidID);
        for (int i = 0; i < machinesList.size(); i++) {
            LatLng point = new LatLng(machinesList.get(i).latitude, machinesList.get(i).longitude);
            Marker m;
            if (machinesList.get(i).status==1) {
                m=mMap.addMarker(new MarkerOptions().position(point).title(String.valueOf(machinesList.get(i).machineID))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).draggable(true));
                m.setTag(0);
            } else {
                if(machinesList.get(i).status==9){
                    m=mMap.addMarker(new MarkerOptions().position(point).title(String.valueOf(machinesList.get(i).machineID))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).draggable(true));
                    m.setTag(0);
                    m.setDraggable(isDraggable);
                }else {
                    m = mMap.addMarker(new MarkerOptions().position(point).title(String.valueOf(machinesList.get(i).machineID)));
                    m.setTag(0);
                    m.setDraggable(isDraggable);
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }
        float zoom = 15;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint,zoom));
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        newPoint = latLng;
        startActivity(new Intent(this, ConfirmNewDevice.class));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        ChangeStatus(marker);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this,"!!!",Toast.LENGTH_LONG).show();
    }


    private void ChangeStatus(final Marker marker){
        dialogChangeStatus = new AlertDialog.Builder(this);

        final String[] strStatus = {"Activated", "NotActivated"};
        dialogChangeStatus.setTitle("Choose status for device");
        dialogChangeStatus.setSingleChoiceItems(strStatus, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String status = strStatus[which];
                switch (status) {
                    case "Activated": intStatus = 4;
                        break;
                    case "NotActivated": intStatus = 5;
                        break;

                }

            }
        });

        dialogChangeStatus.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DB_connect con = new DB_connect();
                con.UpdateStatus(String.valueOf(marker.getTitle()), intStatus);
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
}
