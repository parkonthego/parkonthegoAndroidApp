package edu.scu.smurali.parkonthego;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static edu.scu.smurali.parkonthego.R.*;

public class SelectLocationToReserve extends FragmentActivity {


    private MapFragment mSupportMapFragment;
    private NfcAdapter mNfcAdapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "Nfc Functionality";
    private TextView selectLocation;
    private Button selectLocationReserveButton;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    // GRID VIEW

    GridView grid, grid2;
    String[] web = {
            "START DATE",
            "START TIME",


    };

    String[] web2 = {
            "END DATE",
            "END TIME",
    };

    int[] imageId = {
            R.drawable.calender,
            R.drawable.clock,

    };

    int[] imageId2 = {
            R.drawable.calender,
            R.drawable.clock
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_select_location_to_reserve);

        Intent intent = getIntent();
        final LatLng location = (LatLng) intent.getExtras().get("ltdLng");
       final String title = intent.getExtras().getString("title");
        final LatLng searchedLocation = (LatLng) intent.getExtras().get("searchedLocation");
        selectLocation = (TextView) findViewById(R.id.selectLocation);

        String intentSource= (String) intent.getExtras().get("activityName");
        //       // LatLng location = (LatLng) intent.getExtras().get("ltdLng");
//        String title = intent.getExtras().getString("title");
//        confirmationLocation.setText(title);
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


        //  FragmentManager fragmentManager = getChildFragmentManager();
        if(intentSource.equalsIgnoreCase("LocationsOnMap")) {

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
                            // googleMap.setMapType();

                            // -> marker_latlng // MAKE THIS WHATEVER YOU WANT

                            MarkerOptions custom = new MarkerOptions().position(searchedLocation).title("Marker in location      " + searchedLocation.latitude + "," + searchedLocation.longitude)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                            googleMap.addMarker(custom);

                            googleMap.addMarker(new MarkerOptions().position(location)
                                    .title("location :" + location.latitude + "," + location.longitude));


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


        // GRID VIEW

        GridAdapter adapter = new GridAdapter(SelectLocationToReserve.this, web, imageId);
        grid=(GridView)findViewById(id.selectLocationGrid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(SelectLocationToReserve.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });

        GridAdapter adapter2 = new GridAdapter(SelectLocationToReserve.this, web2, imageId2);
        grid2=(GridView)findViewById(id.selectLocationGrid2);
        grid2.setAdapter(adapter2);

        grid2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(SelectLocationToReserve.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }



        });



        selectLocationReserveButton = (Button)findViewById(R.id.selectLocationReserveButton);

        selectLocationReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLocationToReserve.this, ConfirmationActivity.class);
               // intent.putExtra("locationList",locationList);
                intent.putExtra("location",location);
               // intent.putExtra("searchedLocationLong",searchedLatLng.longitude);
                intent.putExtra("title",title);
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
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }
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


    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
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

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

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
            Log.d("Location", "onPostExecute: "+ result);
            if (result != null) {

                Log.d("Location", "onPostExecute: "+ result);
                /////////////// to do data search from server////////////////////////
                selectLocation.setText("Read content: " + result);
                //////////to be replaced by searchinf array of locations using nfc id////////////////////////////

                final LatLng searchedLocation = new LatLng(37.346317, -121.938025);

                ///////////////////
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
                                // googleMap.setMapType();

                                // -> marker_latlng // MAKE THIS WHATEVER YOU WANT

                                MarkerOptions custom = new MarkerOptions().position(searchedLocation).title("Marker in location      " + searchedLocation.latitude + "," + searchedLocation.longitude)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                                googleMap.addMarker(custom);

//                                googleMap.addMarker(new MarkerOptions().position(location)
//                                        .title("location :" + location.latitude + "," + location.longitude));
//
//
//                                Polyline line = googleMap.addPolyline(new PolylineOptions()
//                                        .add(location,searchedLocation)
//                                        .width(5)
//                                        .color(Color.RED));

                                CameraPosition cameraPosition = new CameraPosition.Builder().target(searchedLocation).zoom(15.0f).build();
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
    }

}
