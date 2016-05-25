package edu.scu.smurali.parkonthego;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import edu.scu.smurali.parkonthego.retrofit.reponses.LoginResponse;
import edu.scu.smurali.parkonthego.retrofit.reponses.SearchData;
import edu.scu.smurali.parkonthego.retrofit.reponses.SearchResponse;
import edu.scu.smurali.parkonthego.retrofit.services.LocationServices;
import edu.scu.smurali.parkonthego.retrofit.services.UserServices;
import edu.scu.smurali.parkonthego.util.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Button searchParkingLocations;
    private LatLng searchedLatLng;
    private  String searchedAddress;
    PlaceAutocompleteFragment autocompleteFragment;
    private Context mContext;
    ArrayList<SearchData> locationList;

   TextView startDate, startTime, endDate, endTime;

    ImageButton startDateButton, endDateButton, startTimeButton, endTimeButton;


//    ///////////////////////////////////////////////////////test code//////////////////////////////////////////////

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
        locationList = new ArrayList<SearchData>();
        //Intent intent = getIntent();
//        final String userId =(String) intent.getExtras().get("userId");
        PreferencesManager pm = PreferencesManager.getInstance(mContext);
        final  int userId= pm.getUserId();


        startTimeButton = (ImageButton) findViewById(R.id.startTimeImageButton);
        endTimeButton = (ImageButton) findViewById(R.id.endTimeImageButton);

        startTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");
            }
        });




        endTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");
            }
        });


        startDateButton = (ImageButton) findViewById(R.id.startDateImageButton);
        endDateButton = (ImageButton) findViewById(R.id.endDateImageButton);

        startDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                newFragment.show(ft, "datePicker");
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                newFragment.show(ft, "datePicker");
            }
        });





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////getting locations near me data from server/////////////////////////////////

//        locationList.add(new Location(1,37.346317, -121.938025, 10, "location1","help1"));
//        locationList.add(new Location(2,37.345325, -121.936551, 10, "location1","help1"));
//        locationList.add(new Location(3,37.345186, -121.936744, 10, "location1","help1"));
//        locationList.add(new Location(4,37.345135, -121.935977, 10, "location1","help1"));
//        locationList.add(new Location(5,37.348650, -121.939345, 10, "location1","help1"));
//        locationList.add(new Location(6,37.3496, -121.9390, 10, "location1","help1"));
//        locationList.add(new Location(7,37.347562, -121.932221, 10, "location1","help1"));



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
                if(searchedLatLng==null)
                {
                    String message=" please select the destination adddress to find parking";
                    String title = " Select Location";
                    ParkOnTheGo.getInstance().showAlert(HomeScreenActivity.this,message,title);
                }
                else{
                    searchLocationsNearMe(userId,searchedLatLng.latitude,searchedLatLng.longitude,5);
                }

//                Intent intent = new Intent(HomeScreenActivity.this, LocationsOnMap.class);
////
////
//                intent.putExtra("locationList",locationList);
//                intent.putExtra("searchedLocationLat",searchedLatLng.latitude);
//                intent.putExtra("searchedLocationLong",searchedLatLng.longitude);
//                intent.putExtra("searchedLocationAddress",searchedAddress);
//                startActivity(intent);
            }
        });


    }

    public void searchLocationsNearMe( int id,
                                       Double lat,
                                       Double lng,
                                       Integer distance) {

        if (ParkOnTheGo.getInstance().isConnectedToInterNet()) {
            LocationServices locationServices = ParkOnTheGo.getInstance().getLocationServices();
//            ParkOnTheGo.getInstance().showProgressDialog(mContext.getString(R.string
//                    .login_signin), mContext.getString(R.string.login_please_wait));
            Call<SearchResponse> call = locationServices.getLocationsNearMe(id, lat,lng,distance);
            Log.d("Calling", "register: " + call);
            call.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call,
                                       Response<SearchResponse> response) {
                    //ParkOnTheGo.getInstance().hideProgressDialog();
                    if (response.isSuccessful()) {
                        parseResponse(response.body());
                    }
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Request failed" + throwable, Toast.LENGTH_SHORT).show();

                    // ParkOnTheGo.getInstance().hideProgressDialog();
                    // ParkOnTheGo.getInstance().handleError(throwable);
                }
            });
        } else {
            ParkOnTheGo.getInstance().showAlert(mContext.getString(R.string.no_network));
        }
    }

    private void parseResponse(SearchResponse response) {
        Toast.makeText(getApplicationContext(), "Login Sucess" + response.getSuccess(), Toast.LENGTH_SHORT).show();
        if (response.getSuccess() == true) {
            PreferencesManager pm = PreferencesManager.getInstance(mContext);
            Log.d("Data", "parseResponse: " + response.getData().size() );

          //  locationList= (ArrayList<SearchData>) response.getData();
            for(int i=0;i<response.getData().size();i++)
            {

                locationList.add(response.getData().get(i));
                Log.d("Data", "parseResponse: " + locationList.get(i).toString());
            }



                Intent intent = new Intent(HomeScreenActivity.this, LocationsOnMap.class);
//
//
                intent.putExtra("locationList", locationList);
                intent.putExtra("searchedLocationLat", searchedLatLng.latitude);
                intent.putExtra("searchedLocationLong", searchedLatLng.longitude);
                intent.putExtra("searchedLocationAddress", searchedAddress);
                startActivity(intent);


////            pm.updateUserId(response.getData().getId());
////            pm.updateUserName(response.getData().getDisplayName());
////            response.getData().getId();
//            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
//            startActivity(intent);


        } else {

        }
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

        if(id == R.id.nav_home){
            Intent intent = new Intent(HomeScreenActivity.this,HomeScreenActivity.class);
            startActivity(intent);
        }

        if (id == R.id.nav_reservation) {

            Intent intent = new Intent(HomeScreenActivity.this,ReservationsActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(HomeScreenActivity.this,SettingActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_call) {

        } else if (id == R.id.nav_help) {

            Intent intent = new Intent(HomeScreenActivity.this,HelpActivity.class);
            startActivity(intent);

        } else if(id == R.id.nav_logout){



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
