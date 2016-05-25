package edu.scu.smurali.parkonthego.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import edu.scu.smurali.parkonthego.ParkOnTheGo;
import edu.scu.smurali.parkonthego.R;
import edu.scu.smurali.parkonthego.retrofit.reponses.ReservationCfnResponse;
import edu.scu.smurali.parkonthego.retrofit.reponses.SearchData;
import edu.scu.smurali.parkonthego.retrofit.services.ReservationServices;
import edu.scu.smurali.parkonthego.util.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    private TextView confirmationLocationTextView;
    private String selectedLocation;
    private String startDateTime, endDateTime;
    private Button cfnReserveButton;
    private Context mContext;
    private SearchData locationObject;

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
            mContext = this;
            // actionBar.setHomeButtonEnabled(true);
        } catch (NullPointerException ex) {
            Log.d("Confirmation:", "onCreate: Null pointer in action bar " + ex.getMessage());
        }

        cfnReserveButton = (Button) findViewById(R.id.confirmationReserveButton);


//        ////////////////////////////////////////////////testing varun raparla/////////////////////////////////////////////////////////////////////////////
        Intent intent = getIntent();
        confirmationLocationTextView = (TextView) findViewById(R.id.confirmationLocationTextView);
        LatLng location = intent.getParcelableExtra("location");
        String title = intent.getExtras().getString("title");
        title = intent.getExtras().getString("title");
        startDateTime = intent.getExtras().getString("startDateTime");
        endDateTime = intent.getExtras().getString("startEndTime");
        selectedLocation = intent.getExtras().getString("selectedLocation");
        Log.d("Json", "onCreate: " + selectedLocation);
        locationObject = new Gson().fromJson(selectedLocation, SearchData.class);
        Log.d("Confirmation Id", "onCreate: " + locationObject.getId());


//       // LatLng location = (LatLng) intent.getExtras().get("ltdLng");
//        String title = intent.getExtras().getString("title");
        confirmationLocationTextView.setText(title);

        cfnReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReservation(locationObject.getId(), PreferencesManager.getInstance(mContext).getUserId(), startDateTime, endDateTime, 0.0);
            }
        });

    }


    public void saveReservation(Integer parkingId,
                                Integer userId,
                                String sDateTime,
                                String eDatetTime, Double cost) {

        if (ParkOnTheGo.getInstance().isConnectedToInterNet()) {
            ReservationServices reservationServices = ParkOnTheGo.getInstance().getReservationServices();
//            ParkOnTheGo.getInstance().showProgressDialog(mContext.getString(R.string
//                    .login_signin), mContext.getString(R.string.login_please_wait));

            Call<ReservationCfnResponse> call = reservationServices.createReservation(parkingId, userId, sDateTime, eDatetTime, cost);
            Log.d("Calling", "register: " + call + " " + parkingId + " " + userId + " " + sDateTime + " " + eDatetTime + " " + cost);
            call.enqueue(new Callback<ReservationCfnResponse>() {
                @Override
                public void onResponse(Call<ReservationCfnResponse> call,
                                       Response<ReservationCfnResponse> response) {
                    //ParkOnTheGo.getInstance().hideProgressDialog();
                    if (response.isSuccessful()) {
                        parseResponse(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ReservationCfnResponse> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Request failed " + throwable, Toast.LENGTH_SHORT).show();
                    Log.d("Failed", "onFailure: " + throwable);

                    // ParkOnTheGo.getInstance().hideProgressDialog();
                    // ParkOnTheGo.getInstance().handleError(throwable);
                }
            });
        } else {
            ParkOnTheGo.getInstance().showAlert(mContext.getString(R.string.no_network));
        }
    }

    private void parseResponse(ReservationCfnResponse response) {
        Toast.makeText(getApplicationContext(), "Login Sucess" + response.getSuccess(), Toast.LENGTH_SHORT).show();
        if (response.getSuccess() == true) {
            ParkOnTheGo.getInstance().showAlert(mContext, "Reservation sucessful|!!! \n Your reservation id " + response.getData().getId(), "Status");
            startActivity(new Intent(ConfirmationActivity.this, HomeScreenActivity.class));
        } else {

        }
    }

}





