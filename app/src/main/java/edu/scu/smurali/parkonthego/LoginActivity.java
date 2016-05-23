package edu.scu.smurali.parkonthego;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.scu.smurali.parkonthego.retrofit.reponses.LoginResponse;
import edu.scu.smurali.parkonthego.retrofit.services.UserServices;
import edu.scu.smurali.parkonthego.util.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public final int permissions = 100;
    private Button login, register;
    private Button maps;
    private TextView forgotPassword;
    private CheckBox stayLoggedIn;
    private EditText email, pwd;
    private Context mContext;
    private PreferencesManager pManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mContext = this;
        pManager = PreferencesManager.getInstance(mContext);
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("ParkOnTheGo");
            actionBar.setIcon(R.mipmap.ic_park);
            //  actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            // actionBar.setHomeButtonEnabled(true);
        } catch (NullPointerException ex) {
            Log.d("Login", "onCreate: Null pointer in action bar " + ex.getMessage());
        }
        login = (Button) findViewById(R.id.logInButton);
        register = (Button) findViewById(R.id.registerButton);

        // maps = (Button)findViewById(R.id.maps);
        forgotPassword = (TextView) findViewById(R.id.forgotPasswordEmail);
        //  maps = (Button)findViewById(R.id.maps);
        stayLoggedIn = (CheckBox) findViewById(R.id.stayLoggedInCheckBox);
        email = (EditText) findViewById(R.id.loignEmailEditText);
        pwd = (EditText) findViewById(R.id.loginPasswordEditText);

        if (pManager.getUserId() > -1) {
            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
            startActivity(intent);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = email.getText().toString();
                String passwordText = pwd.getText().toString();
                login(emailText, passwordText);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


//        maps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//
//
//            }
//        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


        Log.d("**************", "onCreate: " + login);


    }


    public void login(String email, String password) {

        if (ParkOnTheGo.getInstance().isConnectedToInterNet()) {
            UserServices userServices = ParkOnTheGo.getInstance().getUserServices();
//            ParkOnTheGo.getInstance().showProgressDialog(mContext.getString(R.string
//                    .login_signin), mContext.getString(R.string.login_please_wait));
            Call<LoginResponse> call = userServices.login(email, password);
            Log.d("Calling", "register: " + call);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call,
                                       Response<LoginResponse> response) {
                    //ParkOnTheGo.getInstance().hideProgressDialog();
                    if (response.isSuccessful()) {
                        parseResponse(response.body());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Request failed" + throwable, Toast.LENGTH_SHORT).show();

                    // ParkOnTheGo.getInstance().hideProgressDialog();
                    // ParkOnTheGo.getInstance().handleError(throwable);
                }
            });
        } else {
            ParkOnTheGo.getInstance().showAlert(mContext.getString(R.string.no_network));
        }
    }

    private void parseResponse(LoginResponse response) {
        Toast.makeText(getApplicationContext(), "Login Sucess" + response.getSuccess(), Toast.LENGTH_SHORT).show();
        if (response.getSuccess() == true) {
            PreferencesManager pm = PreferencesManager.getInstance(mContext);
            pm.updateUserId(response.getData().getId());
            pm.updateUserName(response.getData().getDisplayName());
            response.getData().getId();
            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
            startActivity(intent);

        } else {

        }
    }


}
