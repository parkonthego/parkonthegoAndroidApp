package edu.scu.smurali.parkonthego;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ConfirmationActivity extends AppCompatActivity {

   private TextView confirmationLocationTextView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("ParkOnTheGo");
            actionBar.setIcon(R.mipmap.ic_park);
            //  actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            // actionBar.setHomeButtonEnabled(true);
        }
        catch(NullPointerException ex){
            Log.d("Confirmation:", "onCreate: Null pointer in action bar "+ex.getMessage());
        }


//        ////////////////////////////////////////////////testing varun raparla/////////////////////////////////////////////////////////////////////////////
     Intent intent  = getIntent();
     confirmationLocationTextView=(TextView)findViewById(R.id.confirmationLocationTextView);
        LatLng location  = intent.getParcelableExtra("location");
        String title = intent.getExtras().getString("title");


//       // LatLng location = (LatLng) intent.getExtras().get("ltdLng");
//        String title = intent.getExtras().getString("title");
        confirmationLocationTextView.setText(title);

    }

}





