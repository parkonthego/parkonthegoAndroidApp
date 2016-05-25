package edu.scu.smurali.parkonthego.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.scu.smurali.parkonthego.ParkOnTheGo;
import edu.scu.smurali.parkonthego.R;
import edu.scu.smurali.parkonthego.retrofit.reponses.SearchData;
import edu.scu.smurali.parkonthego.retrofit.reponses.SearchResponse;
import edu.scu.smurali.parkonthego.retrofit.services.LocationServices;
import edu.scu.smurali.parkonthego.util.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Validator.ValidationListener {
    static int userId;
    @NotEmpty(message = "Please select start date")
    private static TextView startDate;
    @NotEmpty(message = "Please select start time")
    private static TextView startTime;
    @NotEmpty(message = "Please select end date")
    private static TextView endDate;
    @NotEmpty(message = "Please select end time")
    private static TextView endTime;
    PlaceAutocompleteFragment autocompleteFragment;
    ArrayList<SearchData> locationList;
    ImageButton startDateButton, endDateButton, startTimeButton, endTimeButton;
    private Button searchParkingLocations;
    private LatLng searchedLatLng;
    private String searchedAddress;
    private Context mContext;
    private String sDateTime, eDateTime;


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


        } catch (NullPointerException ex) {
            Log.d("Home Screen", "onCreate: Null pointer in action bar " + ex.getMessage());
        }
        final Validator validator = new Validator(this);
        validator.setValidationListener(this);

        startTime = (TextView) findViewById(R.id.homeScreenStartTime);
        endTime = (TextView) findViewById(R.id.homeScreenEndTime);
        startDate = (TextView) findViewById(R.id.homeScreenStartDate);
        endDate = (TextView) findViewById(R.id.homeScreenEndDate);

        locationList = new ArrayList<SearchData>();
        //Intent intent = getIntent();
//        final String userId =(String) intent.getExtras().get("userId");
        PreferencesManager pm = PreferencesManager.getInstance(mContext);
        userId = pm.getUserId();


        startTimeButton = (ImageButton) findViewById(R.id.startTimeImageButton);
        endTimeButton = (ImageButton) findViewById(R.id.endTimeImageButton);

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new StartTimePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");
            }
        });


        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new EndTimePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");
            }
        });


        startDateButton = (ImageButton) findViewById(R.id.startDateImageButton);
        endDateButton = (ImageButton) findViewById(R.id.endDateImageButton);

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new StartDatePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "datePicker");
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new EndDatePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
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

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                searchedLatLng = place.getLatLng();
                searchedAddress = place.getAddress().toString();


                Log.i("place name", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {

                Log.i("error ", "An error occurred: " + status);
            }
        });


        searchParkingLocations = (Button) findViewById(R.id.searchParkingLocation);
        searchParkingLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchedLatLng == null) {
                    String message = " please select the destination adddress to find parking";
                    String title = " Select Location";
                    ParkOnTheGo.getInstance().showAlert(HomeScreenActivity.this, message, title);
                } else {
                    validator.validate();

                }

            }
        });


    }

    @Override
    public void onValidationSucceeded() {
        String startDateValue = startDate.getText().toString();
        String startTimeValue = startTime.getText().toString();
        String endDateValue = endDate.getText().toString();
        String endTimeValue = endTime.getText().toString();
        sDateTime = startDateValue + " " + startTimeValue;
        eDateTime = endDateValue + " " + endTimeValue;


        searchLocationsNearMe(userId, searchedLatLng.latitude, searchedLatLng.longitude, 5, sDateTime, eDateTime);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextColor(Color.RED);
                toast.show();
                // Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void searchLocationsNearMe(int id,
                                      Double lat,
                                      Double lng,
                                      Integer distance, String sDateTime, String eDateTime) {

        if (ParkOnTheGo.getInstance().isConnectedToInterNet()) {
            LocationServices locationServices = ParkOnTheGo.getInstance().getLocationServices();
//            ParkOnTheGo.getInstance().showProgressDialog(mContext.getString(R.string
//                    .login_signin), mContext.getString(R.string.login_please_wait));
            Call<SearchResponse> call = locationServices.getLocationsNearMe(id, lat, lng, distance, sDateTime, eDateTime);
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
            Log.d("Data", "parseResponse: " + response.getData().size());

            //  locationList= (ArrayList<SearchData>) response.getData();
            for (int i = 0; i < response.getData().size(); i++) {

                locationList.add(response.getData().get(i));
                Log.d("Data", "parseResponse: " + locationList.get(i).toString());
            }
            Intent intent = new Intent(HomeScreenActivity.this, LocationsOnMap.class);
            intent.putExtra("locationList", locationList);
            intent.putExtra("searchedLocationLat", searchedLatLng.latitude);
            intent.putExtra("searchedLocationLong", searchedLatLng.longitude);
            intent.putExtra("searchedLocationAddress", searchedAddress);
            Log.d("Home screen", "parseResponse: " + sDateTime);
            Log.d("Home screen", "parseResponse: " + eDateTime);
            intent.putExtra("startDateTime", sDateTime);
            intent.putExtra("endDateTime", eDateTime);
            startActivity(intent);


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

        if (id == R.id.nav_home) {
            Intent intent = new Intent(HomeScreenActivity.this, HomeScreenActivity.class);
            startActivity(intent);
        }

        if (id == R.id.nav_reservation) {

            Intent intent = new Intent(HomeScreenActivity.this, ReservationsActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(HomeScreenActivity.this, SettingActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_call) {

        } else if (id == R.id.nav_help) {

            Intent intent = new Intent(HomeScreenActivity.this, HelpActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    /*DatePicker and TimePicker code starts here*/


    public void showTimePickerDialog(View v) {


        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new StartTimePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");


            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new EndTimePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");

            }
        });


    }

    public void showDatePickerDialog(View v) {


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new StartDatePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "datePicker");

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new EndDatePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "datePicker");
            }
        });


    }

    public static class StartDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

//        private TextView homeStartDate, homeEndDate;

        public StartDatePickerFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            //String date = month + "-" + day + "-" + "year";


            startDate.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));


        }
    }

    public static class EndDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

//        private TextView homeStartDate, homeEndDate;

        public EndDatePickerFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            //String date = month + "-" + day + "-" + "year";


            endDate.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));


        }
    }

    public static class StartTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        TextView homeStartTime, homeEndTime;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            String time = hourOfDay + ":" + minute;

            startTime.setText(time);
//            homeEndTime.setText(time);
        }
    }

    public static class EndTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        TextView homeStartTime, homeEndTime;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            String time = hourOfDay + ":" + minute;

//            homeStartTime.setText(time);
            endTime.setText(time);
        }
    }


}