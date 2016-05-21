package edu.scu.smurali.parkonthego;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.HashMap;

import edu.scu.smurali.parkonthego.retrofit.reponses.SignUpResponse;
import edu.scu.smurali.parkonthego.retrofit.services.UserServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ParkOnTheGo");
        actionBar.setIcon(R.mipmap.ic_park);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        this.mContext = this;
    }


    public void register(HashMap data) {
        if (ParkOnTheGo.getInstance().isConnectedToInterNet()) {
            UserServices userServices = ParkOnTheGo.getInstance().getUserServices();
//            ParkOnTheGo.getInstance().showProgressDialog(mContext.getString(R.string
//                    .login_signin), mContext.getString(R.string.login_please_wait));
            //Call<SignUpResponse> call = userServices.createNewUser(userName, password, firstName, lastName);
//            call.enqueue(new Callback<SignUpResponse>() {
//                @Override
//                public void onResponse(Call<SignUpResponse> call,
//                                       Response<SignUpResponse> response) {
//                    ParkOnTheGo.getInstance().hideProgressDialog();
//                    if (response.isSuccessful()) {
//                        parseResponse(response.body());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<SignUpResponse> call, Throwable throwable) {
//                    // ParkOnTheGo.getInstance().hideProgressDialog();
//                    // ParkOnTheGo.getInstance().handleError(throwable);
//                }
//            });
//        } else {
//            ParkOnTheGo.getInstance().showAlert(mContext.getString(R.string.no_network));
        }
    }

    private void parseResponse(SignUpResponse response) {
        if (response.getSuccess().equals("true")) {

            Intent intent = new Intent(RegisterActivity.this, HomeScreenActivity.class);
            startActivity(intent);

        } else {

        }
    }
}
