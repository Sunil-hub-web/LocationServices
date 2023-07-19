package com.example.locationservices;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.locationservices.databinding.ActivityDrawPolygonBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class DrawPolygon extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityDrawPolygonBinding binding;
    Polygon polygon = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    ArrayList<Location> latLngArrayListArea = new ArrayList<>();
    Double value2 = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDrawPolygonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       /* binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    if (polygon == null) return;

                    polygon.setFillColor(Color.BLUE);
                } else {
                    polygon.setFillColor(Color.TRANSPARENT);
                }
            }
        });*/

      /*  binding.btnDrawPolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             *//*   if (polygon != null){
                    polygon.remove();
                }*//*

                PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).clickable(true);
                polygon = mMap.addPolygon(polygonOptions);
                polygon.setFillColor(Color.BLUE);
                polygon.setStrokeColor(Color.RED);

                if (binding.checkBox.isChecked()){
                    polygon.setFillColor(Color.BLUE);
                }
            }
        });*/

        binding.btnClearPolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (polygon != null) {
                    polygon.remove();
                }

                for (Marker marker : markerList) marker.remove();
                latLngList.clear();
                markerList.clear();
                binding.checkBox.setChecked(false);

            }
        });
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

        // Add a marker in Sydney and move the camera
      /*  LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                Marker marker = mMap.addMarker(markerOptions);
                latLngList.add(latLng);
                markerList.add(marker);

                if (polygon != null) {
                    polygon.remove();
                }

                PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).clickable(true);
                polygon = mMap.addPolygon(polygonOptions);
                polygon.setFillColor(Color.BLUE);
                polygon.setStrokeColor(Color.RED);

                calculation(latLngList);
            }
        });
    }

    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    public void calculation(List<LatLng> latLngList) {

        if (latLngList.size() < 3) {

            latLngArrayListArea = new ArrayList<>();

            for (int i = 0; i < latLngList.size(); i++) {

                Location loc1 = new Location("");
                loc1.setLatitude(latLngList.get(i).latitude);
                loc1.setLongitude(latLngList.get(i).longitude);

                latLngArrayListArea.add(loc1);

                Log.d("DrawPolygon","calculation"+latLngArrayListArea.toString());
            }

            for (int j = 0; j < latLngArrayListArea.size(); j++) {

                for (int k = 1; k < latLngArrayListArea.size(); j++) {

                    double latitude1 = latLngArrayListArea.get(j).getLatitude();
                    double longitude1 = latLngArrayListArea.get(j).getLongitude();

                    double latitude2 = latLngArrayListArea.get(k).getLatitude();
                    double longitude2 = latLngArrayListArea.get(k).getLongitude();

                    Double value1 = haversine(latitude1,longitude1,latitude2,longitude2);

                    value2 = value1 + value2;

                    Log.d("DrawPolygon","calculation1"+latitude1+", "+longitude1+" latlng "+latitude2+", "+longitude2);

                }
            }

            Log.d("DrawPolygon","calculation3"+ value2 );

        }else{

            Log.d("DrawPolygon","calculation4"+ "Select Your Data" );
        }
    }
}