package edu.scu.smurali.parkonthego;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import android.util.Log;

public class ConfirmationActivity extends AppCompatActivity {

   private TextView confirmationLocation ;

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
//        Intent intent  = getIntent();
//        confirmationLocation=(TextView)findViewById(R.id.confirmationLocationTextView);
//       // LatLng location = (LatLng) intent.getExtras().get("ltdLng");
//        String title = intent.getExtras().getString("title");
//        confirmationLocation.setText(title);



    }
}
