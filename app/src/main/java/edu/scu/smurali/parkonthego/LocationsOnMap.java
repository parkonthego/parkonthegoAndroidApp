package edu.scu.smurali.parkonthego;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class LocationsOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public final int permissions = 100;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(LocationsOnMap.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    permissions);



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

       Intent intent = getIntent();
       ArrayList<Location> locationList= (ArrayList<Location>) intent.getSerializableExtra("locationList");
        double searchedLocationLat = (Double) intent.getSerializableExtra("searchedLocationLat");
        double searchedLocationLong = (Double) intent.getSerializableExtra("searchedLocationLong");
        String searchedLocationAddress = intent.getStringExtra("searchedLocationAddress");

        final LatLng searchedLocation = new LatLng(searchedLocationLat,searchedLocationLong);


        //locationList.


        // Add a marker in Sydney and move the camera


        MarkerOptions custom = new MarkerOptions().position(new LatLng(searchedLocationLat,searchedLocationLong)).title("Marker in location      "+searchedLocationAddress)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mMap.addMarker(custom);
       for(int i=0;i<locationList.size();i++)
       {
           mMap.addMarker(new MarkerOptions().position(new LatLng(locationList.get(i).getLatitude(),locationList.get(i).getLongitude()))
                                                            .title("location :"+locationList.get(i).getAddress()+
                                                                    "Rate per Hour: "+locationList.get(i).getPrice()));
       }


     //   mMap.addMarker(new MarkerOptions().position(locationList.get(4)).rotation(15));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(searchedLocationLat,searchedLocationLong)));



        mapAnimationToLocation(new LatLng(searchedLocationLat,searchedLocationLong));

       // mMap.moveCamera(CameraUpdateFactory.newLatLng(santaclarauniversity));
        try {
            mMap.setMyLocationEnabled(true);
        }
        catch (SecurityException $ex){
            Log.d("Location permission*S", "onMapReady:Permission not given ");

        }
            mMap.getUiSettings().setMyLocationButtonEnabled(true);













        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                LatLng clickedLocation= marker.getPosition();
                String title = marker.getTitle();
                Intent intent = new Intent(LocationsOnMap.this ,SelectLocationToReserve.class);
                intent.putExtra("ltdLng",clickedLocation);
                intent.putExtra("title", title);
                intent.putExtra("searchedLocation",searchedLocation);
                intent.putExtra("activityName","LocationsOnMap");

                startActivity(intent);

            }
        });

    }
   // method to animate camera to specified location
    private void mapAnimationToLocation(LatLng location) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }
//    public void highlightMarker(Marker marker, Boolean highlight ) {
//        int color = "#FE7569";
//        if (highlight) {
//            color = "#0000FF";
//        }
//        marker.setImage(getIcon(color).image);
//    }

    }




