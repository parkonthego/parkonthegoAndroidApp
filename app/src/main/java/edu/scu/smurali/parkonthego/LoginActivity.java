package edu.scu.smurali.parkonthego;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button login, register,maps;
    private TextView forgotPassword;
    private CheckBox stayLoggedIn;
    private EditText email, pwd;
    public final int permissions = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("ParkOnTheGo");
            actionBar.setIcon(R.mipmap.ic_park);
          //  actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
           // actionBar.setHomeButtonEnabled(true);
        }
        catch(NullPointerException ex){
            Log.d("Login", "onCreate: Null pointer in action bar "+ex.getMessage());
        }
        login = (Button) findViewById(R.id.logInButton);
        register = (Button) findViewById(R.id.registerButton);
        maps = (Button)findViewById(R.id.maps);
        forgotPassword = (TextView) findViewById(R.id.forgotPasswordTextView);
        stayLoggedIn = (CheckBox) findViewById(R.id.stayLoggedInCheckBox);
        email = (EditText) findViewById(R.id.eMailEditText);
        pwd = (EditText) findViewById(R.id.passwordEditText);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(LoginActivity.this, LocationsOnMap.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });




        Log.d("**************", "onCreate: "+login);


    }




}
