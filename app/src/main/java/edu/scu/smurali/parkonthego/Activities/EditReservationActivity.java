package edu.scu.smurali.parkonthego.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.scu.smurali.parkonthego.ParkOnTheGo;
import edu.scu.smurali.parkonthego.R;
import edu.scu.smurali.parkonthego.retrofit.reponses.LocationData;
import edu.scu.smurali.parkonthego.retrofit.reponses.LocationResponse;
import edu.scu.smurali.parkonthego.retrofit.reponses.ReservationData;
import edu.scu.smurali.parkonthego.retrofit.reponses.SearchData;
import edu.scu.smurali.parkonthego.retrofit.services.LocationServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReservationActivity extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "Nfc Functionality";
    private static TextView startDate;
    private static TextView startTime;
    private static TextView endDate;
    private static TextView endTime;
    LocationData recognisedLocation;
    //    private TextView selectLocationStartDate;
//    private TextView selectLocationEndDate;
//    private TextView selectLocationStartTime;
//    private TextView selectLocationEndTime;
//
    private MapFragment mSupportMapFragment;
    private NfcAdapter mNfcAdapter;
    private TextView selectLocation, price;
    private Button selectLocationReserveButton;
    private Context mContext;
    private String sDateTime = "", eDateTime = "";
    private String selectedLocation;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_to_reserve);

        Intent intent = getIntent();
        final LatLng location = (LatLng) intent.getExtras().get("ltdLng");
        final String title = intent.getExtras().getString("title");
        final LatLng searchedLocation = (LatLng) intent.getExtras().get("searchedLocation");
        final String searchedLocationAddress = intent.getStringExtra("searchedLocationAddress");
        final ReservationData reservationData = (ReservationData)intent.getSerializableExtra("reservationData");
        selectedLocation = intent.getStringExtra("selectedLocationObject");
        Log.d("SelectionLocation json", "onCreate: " + selectedLocation);
        selectLocation = (TextView) findViewById(R.id.selectLocation);
        price = (TextView)findViewById(R.id.pricePerHour);

        startDate = (TextView) findViewById(R.id.selectLocationStartDate);
        startTime = (TextView) findViewById(R.id.selectLocationStartTime);
        endDate = (TextView) findViewById(R.id.selectLocationEndDate);
        endTime = (TextView) findViewById(R.id.selectLocationEndTime);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new StartDatePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "datePicker");


            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new StartTimePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");

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
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new EndTimePickerFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                newFragment.show(ft, "timePicker");


            }
        });




        ArrayList<SearchData> locationList = (ArrayList<SearchData>) intent.getSerializableExtra("listOfLocations");


        ////////////////////////////////////////////// to set price and other variables in positions//////////////////////////






        String intentSource= (String) intent.getExtras().get("activityName");
        //       // LatLng location = (LatLng) intent.getExtras().get("ltdLng");
//        String title = intent.getExtras().getString("title");
//        confirmationLocation.setText(title);


        //  FragmentManager fragmentManager = getChildFragmentManager();

        /////////////////// if intent is from locations_on_maps activity////////////////////////////////////////////////////////
        if(intentSource!=null)
            if(intentSource.equalsIgnoreCase("LocationsOnMap")) {
                //Date & time picker
                sDateTime = intent.getStringExtra("startDateTime");
                eDateTime = intent.getStringExtra("endDateTime");
                String[] t = sDateTime.split(" ");
                List<String> sDateTimeList = Arrays.asList(t);
                t = eDateTime.split(" ");
                List<String> eDateTimeList = Arrays.asList(t);
                Log.d("Select location details", "onCreate: " + intent.getStringExtra("startDateTime"));
                startDate.setText(sDateTimeList.get(0));
                startTime.setText(sDateTimeList.get(1));
                endDate.setText(eDateTimeList.get(0));
                endTime.setText(eDateTimeList.get(1));
                SearchData locationSelected = new SearchData();

//                for(int i=0;i<locationList.size();i++)
//                {
//                    if(locationList.get(i).getLatitude()==location.latitude&&locationList.get(i).getLongitude()==location.longitude)
//                    {
//                        locationSelected= locationList.get(i);
//                    }
//                }

//                String priceString = new Double(locationSelected.getPrice()).toString();
                selectLocation.setText(searchedLocationAddress);
                price.setText(reservationData.getPrice().toString());
                final String selectedLocationDescription = locationSelected.getDescription();



                mSupportMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrameLayout);
                if (mSupportMapFragment == null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    mSupportMapFragment = MapFragment.newInstance();
                    fragmentTransaction.replace(R.id.mapFrameLayout, mSupportMapFragment).commit();
                }

                if (mSupportMapFragment != null) {
                    mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if (googleMap != null) {

                                googleMap.getUiSettings().setAllGesturesEnabled(true);
                                // googleMap.setMapType();

                                // -> marker_latlng // MAKE THIS WHATEVER YOU WANT

                                MarkerOptions custom = new MarkerOptions().position(searchedLocation).title("" + searchedLocationAddress)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                                googleMap.addMarker(custom);

                                googleMap.addMarker(new MarkerOptions().position(location)
                                        .title(""+selectedLocationDescription ));


                                Polyline line = googleMap.addPolyline(new PolylineOptions()
                                        .add(location, searchedLocation)
                                        .width(5)
                                        .color(Color.RED));

                                CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(15.0f).build();
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                googleMap.moveCamera(cameraUpdate);
                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        ////////////////////////////////////////////////////////////////
                                    }
                                });

                            }

                        }
                    });


                }
                client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
            }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        selectLocationReserveButton = (Button)findViewById(R.id.selectLocationReserveButton);

        selectLocationReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SelectLocationToReserve Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://edu.scu.smurali.parkonthego/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SelectLocationToReserve Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://edu.scu.smurali.parkonthego/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */

    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */


        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */

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


            startDate.setText(new StringBuilder().append(month).append("/")
                    .append(day).append("/").append(year));


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


            endDate.setText(new StringBuilder().append(month).append("/")
                    .append(day).append("/").append(year));



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
            String min = String.format("%02d", minute);

            String time = hourOfDay + ":" + min;

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
            String min = String.format("%02d", minute);

            String time = hourOfDay + ":" + min;

//            homeStartTime.setText(time);
            endTime.setText(time);
        }
    }


}
