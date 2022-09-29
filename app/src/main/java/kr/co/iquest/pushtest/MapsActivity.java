package kr.co.iquest.pushtest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import kr.co.iquest.pushtest.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,//추가 위치버튼을 눌렀을 때 이러나는 이벤트 정의
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMyLocationClickListener,//추가
        OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /*추가부분 */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResult){

        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if(requestCode == MY_LOCATION_REQUEST_CODE){
            if(permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                if(checkCallingPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        checkCallingPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=
                                PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                mMap.setMyLocationEnabled(true);
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 추가코드
        if(checkCallingPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},1);

        }
        mMap.setOnMapClickListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);


    }

    @Override
    public boolean onMyLocationButtonClick() {

        Toast.makeText(this,"MyLocation clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this,"Current location:" + location, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Toast.makeText(this,"onMapClick :" + latLng, Toast.LENGTH_LONG).show();
    }
}