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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
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
import edu.scu.smurali.parkonthego.retrofit.reponses.SearchData;
import edu.scu.smurali.parkonthego.retrofit.services.LocationServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static edu.scu.smurali.parkonthego.R.id;
import static edu.scu.smurali.parkonthego.R.layout;

public class SelectLocationToReserve extends FragmentActivity {


    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "Nfc Functionality";
    private static TextView startDate;
    private static TextView startTime;
    private static TextView endDate;
    private static TextView endTime;
    public LocationData recognisedLocation;

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

    /////////////set up  foreground dispach of nfc record//////////////////////////

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    ///////////////////// stop fore ground dispatch of nfc /////////////////////////////

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_select_location_to_reserve);

        Intent intent = getIntent();
        final LatLng location = (LatLng) intent.getExtras().get("ltdLng");
        final String title = intent.getExtras().getString("title");
        final LatLng searchedLocation = (LatLng) intent.getExtras().get("searchedLocation");
        final String searchedLocationAddress = intent.getStringExtra("searchedLocationAddress");
        selectedLocation = intent.getStringExtra("selectedLocationObject");
        Log.d("SelectionLocation json", "onCreate: " + selectedLocation);
        selectLocation = (TextView) findViewById(R.id.selectLocation);
        price = (TextView) findViewById(id.pricePerHour);

// date and time pickers////////////////////////////////////////////////////////////

        startDate = (TextView) findViewById(id.selectLocationStartDate);
        startTime = (TextView) findViewById(id.selectLocationStartTime);
        endDate = (TextView) findViewById(id.selectLocationEndDate);
        endTime = (TextView) findViewById(id.selectLocationEndTime);

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


//       get the activity name of the intent
        String intentSource = (String) intent.getExtras().get("activityName");

        // checking if nfc adapter is null
        if (this.mNfcAdapter == null) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();

            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "NFC is enabled.", Toast.LENGTH_LONG).show();
        }

        handleIntent(getIntent());

        /////////////////// if intent is from locations_on_maps activity////////////////////////////////////////////////////////
        if (intentSource != null)
            if (intentSource.equalsIgnoreCase("LocationsOnMap")) {
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

                // get the location which is selected from the map
                for (int i = 0; i < locationList.size(); i++) {
                    if (locationList.get(i).getLatitude() == location.latitude && locationList.get(i).getLongitude() == location.longitude) {
                        locationSelected = locationList.get(i);
                    }
                }
                // set the details of the location in the view
                String priceString = new Double(locationSelected.getPrice()).toString();
                selectLocation.setText(locationSelected.getDescription());
                price.setText(priceString);
                final String selectedLocationDescription = locationSelected.getDescription();

                // set the map fragment with the searched and selected locations with a path between them

                mSupportMapFragment = (MapFragment) getFragmentManager().findFragmentById(id.mapFrameLayout);
                if (mSupportMapFragment == null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    mSupportMapFragment = MapFragment.newInstance();
                    fragmentTransaction.replace(id.mapFrameLayout, mSupportMapFragment).commit();
                }

                if (mSupportMapFragment != null) {
                    mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if (googleMap != null) {

                                googleMap.getUiSettings().setAllGesturesEnabled(true);
                                MarkerOptions custom = new MarkerOptions().position(searchedLocation).title("" + searchedLocationAddress)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                                googleMap.addMarker(custom);

                                googleMap.addMarker(new MarkerOptions().position(location)
                                        .title("" + selectedLocationDescription));


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

        // reserve button
        selectLocationReserveButton = (Button) findViewById(R.id.selectLocationReserveButton);

        selectLocationReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SelectLocationToReserve.this, ConfirmationActivity.class);
                intent.putExtra("location", location);
                String startDateTime = startDate.getText().toString() + " " + startTime.getText().toString();
                String endDateTime = endDate.getText().toString() + " " + endTime.getText().toString();
                intent.putExtra("title", title);
                intent.putExtra("startDateTime", startDateTime);
                intent.putExtra("startEndTime", endDateTime);
                intent.putExtra("selectedLocation", selectedLocation);
                startActivity(intent);
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
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }
// handle intent for nfc

    private void handleIntent(Intent intent) {
        // TODO: handle Intent
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    // //////////////////////////////////date and time picker fragments ////////////////////////////////////////////////////////
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
            int month = c.get(Calendar.MONTH) + 1;
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
            int month = c.get(Calendar.MONTH) + 1;
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
    /////////////////////////////////////////// date and time picker fragments end///////////////////////////////////////////////


    // class  to read  NDEF recorn from the nfc device/tag/////////////////////////
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }


        // read text record from the nfc tag containing the location onformation//////////////////////////

        private String readText(NdefRecord record) throws UnsupportedEncodingException {

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }


        @Override
        protected void onPostExecute(String result) {
            Log.d("Location", "onPostExecute: " + result);
            if (result != null) {

                Log.d("Location", "onPostExecute: " + result);

                findLocationByID(result);

                // set up the map frgament if the intent is from the nfc tag
                mSupportMapFragment = (MapFragment) getFragmentManager().findFragmentById(id.mapFrameLayout);
                if (mSupportMapFragment == null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    mSupportMapFragment = MapFragment.newInstance();
                    fragmentTransaction.replace(id.mapFrameLayout, mSupportMapFragment).commit();
                }

                if (mSupportMapFragment != null) {
                    mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            if (googleMap != null) {

                                googleMap.getUiSettings().setAllGesturesEnabled(true);
                                // set the map marker at the location of the parking
                                MarkerOptions custom = new MarkerOptions().position(new LatLng(recognisedLocation.getLatitude(), recognisedLocation.getLongitude()))
                                        .title("" + recognisedLocation.getDescription())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                                googleMap.addMarker(custom);

                                // set the camera to the location plotted on map
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(recognisedLocation.getLatitude(), recognisedLocation.getLongitude())).zoom(15.0f).build();
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
                    client = new GoogleApiClient.Builder(SelectLocationToReserve.this).addApi(AppIndex.API).build();


                }
            }
        }
        // method to find the location object using location id
        public void findLocationByID( String id) {

            if (ParkOnTheGo.getInstance().isConnectedToInterNet()) {
                LocationServices locationServices = ParkOnTheGo.getInstance().getLocationServices();
                Call<LocationResponse> call = locationServices.getLocationDetails(id);
                Log.d("Calling", "register: " + call);
                call.enqueue(new Callback<LocationResponse>() {
                    @Override
                    public void onResponse(Call<LocationResponse> call,
                                           Response<LocationResponse> response) {

                        if (response.isSuccessful()) {
                            parseResponse(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationResponse> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Request failed" + throwable, Toast.LENGTH_SHORT).show();

                        // ParkOnTheGo.getInstance().hideProgressDialog();
                        // ParkOnTheGo.getInstance().handleError(throwable);
                    }
                });
            } else {
                ParkOnTheGo.getInstance().showAlert(mContext.getString(R.string.no_network));
            }
        }

        private void parseResponse(LocationResponse response) {
            Toast.makeText(getApplicationContext(), "Login Sucess" + response.getSuccess(), Toast.LENGTH_SHORT).show();
            if (response.getSuccess() == true) {
                //PreferencesManager pm = PreferencesManager.getInstance(mContext);
                // Log.d("Data", "parseResponse: " + response.getData().size() );

                recognisedLocation = response.getData();
                String priceString = new Double(recognisedLocation.getPrice()).toString();

                selectLocation.setText(recognisedLocation.getDescription());
                price.setText(priceString);
                selectedLocation= new Gson().toJson(recognisedLocation);



            }
            else{
                Toast.makeText(getApplicationContext(), "retrival fail" + response.getSuccess(), Toast.LENGTH_SHORT).show();
            }
        }
    }





    }



