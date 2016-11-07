package ua.com.alternatiview.parkeonservice;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LinkedList<Device> machinesList = new LinkedList<Device>() ;
    DB_connect con = new DB_connect();

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
        con.DropTempTable(androidID);
        // Add a marker in Sydney and move the camera
        LatLng startPoint = new LatLng(machinesList.get(0).latitude, machinesList.get(0).longitude);
        for (int i = 0; i < machinesList.size(); i++) {
            LatLng point = new LatLng(machinesList.get(i).latitude, machinesList.get(i).longitude);
            if (machinesList.get(i).status>0) {
                mMap.addMarker(new MarkerOptions().position(point).title(String.valueOf(machinesList.get(i).machineID))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else
                mMap.addMarker(new MarkerOptions().position(point).title(String.valueOf(machinesList.get(i).machineID)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
