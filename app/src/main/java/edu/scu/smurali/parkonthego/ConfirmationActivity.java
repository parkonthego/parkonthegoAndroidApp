package edu.scu.smurali.parkonthego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class ConfirmationActivity extends AppCompatActivity {

   private TextView confirmationLocation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ////////////////////////////////////////////////testing varun raparla/////////////////////////////////////////////////////////////////////////////
        Intent intent  = getIntent();
        confirmationLocation=(TextView)findViewById(R.id.confirmationLocationTextView);
       // LatLng location = (LatLng) intent.getExtras().get("ltdLng");
        String title = intent.getExtras().getString("title");
        confirmationLocation.setText(title);



    }
}
