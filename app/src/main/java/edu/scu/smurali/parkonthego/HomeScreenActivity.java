package edu.scu.smurali.parkonthego;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
   private Button searchParkingLocations;
    private LatLng searchedLatLng;
    private  String searchedAddress;
    PlaceAutocompleteFragment autocompleteFragment;

    TextView startDate, startTime, endDate, endTime;

//    GridView grid;
//    String[] web = {
//            "START DATE",
//            "START TIME",
//            "END DATE",
//            "END TIME",
//
//    } ;
//    int[] imageId = {
//            R.drawable.calender,
//            R.drawable.clock,
//
//    };

    ///////////////////////////////////////////////////////test code//////////////////////////////////////////////

    ArrayList<Location> locationList = new ArrayList<Location>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("ParkOnTheGo");
            actionBar.setIcon(R.mipmap.ic_park);
            //  actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            // actionBar.setHomeButtonEnabled(true);


        }
        catch(NullPointerException ex){
            Log.d("Home Screen", "onCreate: Null pointer in action bar "+ex.getMessage());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////tst data//////////////////////////////////

        locationList.add(new Location(1,37.346317, -121.938025, 10, "location1","help1"));
        locationList.add(new Location(2,37.345325, -121.936551, 10, "location1","help1"));
        locationList.add(new Location(3,37.345186, -121.936744, 10, "location1","help1"));
        locationList.add(new Location(4,37.345135, -121.935977, 10, "location1","help1"));
        locationList.add(new Location(5,37.348650, -121.939345, 10, "location1","help1"));
        locationList.add(new Location(6,37.3496, -121.9390, 10, "location1","help1"));
        locationList.add(new Location(7,37.347562, -121.932221, 10, "location1","help1"));


        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                searchedLatLng=place.getLatLng();
                searchedAddress= place.getAddress().toString();


                Log.i("place name", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {

                Log.i("error ", "An error occurred: " + status);
            }
        });


        searchParkingLocations = (Button)findViewById(R.id.searchParkingLocation);
        searchParkingLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, LocationsOnMap.class);
                intent.putExtra("locationList",locationList);
                intent.putExtra("searchedLocationLat",searchedLatLng.latitude);
                intent.putExtra("searchedLocationLong",searchedLatLng.longitude);
                intent.putExtra("searchedLocationAddress",searchedAddress);
                startActivity(intent);
            }
        });

//        GridAdapter adapter = new GridAdapter(HomeScreenActivity.this, web, imageId);
//        grid=(GridView)findViewById(R.id.grid);
//        grid.setAdapter(adapter);
//
//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(HomeScreenActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
//
//            }
//        });



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_uninstall) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reservation) {


            Intent intent = new Intent(HomeScreenActivity.this,MyReservationActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_call) {

        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*DatePicker and TimePicker code starts here*/


    public void showTimePickerDialog(View v) {

        startTime = (TextView) findViewById(R.id.homeScreenStartTime);
        endTime = (TextView) findViewById(R.id.homeScreenEndTime);

        startTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");
            }
        });


    }

    public void showDatePickerDialog(View v) {

        startDate = (TextView) findViewById(R.id.homeScreenStartDate);
        endDate = (TextView) findViewById(R.id.homeScreenEndDate);

        startDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                newFragment.show(ft, "datePicker");
            }
        });

        endDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                newFragment.show(ft, "datePicker");
            }
        });


}





}
