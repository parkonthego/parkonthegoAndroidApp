package edu.scu.smurali.parkonthego;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import edu.scu.smurali.parkonthego.retrofit.reponses.SignUpResponse;
import edu.scu.smurali.parkonthego.retrofit.services.UserServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Context mContext;
    private EditText regFirstName, regLastName, regEmail, regPassword, regCfnPassword;
    private Button regButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("ParkOnTheGo");
            actionBar.setIcon(R.mipmap.ic_park);
            //  actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            // actionBar.setHomeButtonEnabled(true);
            this.regFirstName = (EditText) findViewById(R.id.regFirstName);
            this.regLastName = (EditText) findViewById(R.id.regLastName);
            this.regEmail = (EditText) findViewById(R.id.regEmail);
            this.regPassword = (EditText) findViewById(R.id.regPassword);
            this.regCfnPassword = (EditText) findViewById(R.id.regCfnPassword);

            this.regButton = (Button) findViewById(R.id.regRegisterButton);

            this.regButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap data;
                    data = new HashMap();
                    data.put("firstName", regFirstName.getText().toString());
                    data.put("lastName", regFirstName.getText().toString());
                    data.put("email", regEmail.getText().toString());
                    data.put("password", regPassword.getText().toString());
                    register(data);

                }
            });


        } catch (NullPointerException ex) {
            Log.d("RegisterActivity", "onCreate: Null pointer in action bar " + ex.getMessage());
        }
        this.mContext = this;
    }


    public void register(HashMap data) {

        if (ParkOnTheGo.getInstance().isConnectedToInterNet()) {
            UserServices userServices = ParkOnTheGo.getInstance().getUserServices();
//            ParkOnTheGo.getInstance().showProgressDialog(mContext.getString(R.string
//                    .login_signin), mContext.getString(R.string.login_please_wait));
            Call<SignUpResponse> call = userServices.createNewUser(data.get("firstName").toString(), data.get("lastName").toString(), data.get("email").toString(), data.get("password").toString());
            Log.d("Calling", "register: " + call);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call,
                                       Response<SignUpResponse> response) {
                    ParkOnTheGo.getInstance().hideProgressDialog();
                    if (response.isSuccessful()) {
                        parseResponse(response.body());
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Request failed" + throwable, Toast.LENGTH_SHORT).show();

                    // ParkOnTheGo.getInstance().hideProgressDialog();
                    // ParkOnTheGo.getInstance().handleError(throwable);
                }
            });
        } else {
            ParkOnTheGo.getInstance().showAlert(mContext.getString(R.string.no_network));
        }
    }

    private void parseResponse(SignUpResponse response) {
        Toast.makeText(getApplicationContext(), "Request Sucess" + response.getSuccess(), Toast.LENGTH_SHORT).show();
        if (response.getSuccess() == true) {
            Intent intent = new Intent(RegisterActivity.this, HomeScreenActivity.class);
            startActivity(intent);

        } else {

        }
    }
}
