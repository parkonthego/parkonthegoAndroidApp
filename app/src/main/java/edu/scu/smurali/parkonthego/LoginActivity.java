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

   //  Button login =(Button) findViewById(R.id.logInButton);
//     Button register = (Button) findViewById(R.id.registerButton);
//     TextView forgotPassword = (TextView) findViewById(R.id.forgotPasswordTextView);
//     CheckBox stayLoggedIn = (CheckBox) findViewById(R.id.stayLoggedInCheckBox);
//     EditText email = (EditText) findViewById(R.id.eMailEditText);
//     EditText pwd = (EditText) findViewById(R.id.passwordEditText);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ParkOnTheGo");
        actionBar.setIcon(R.mipmap.ic_park);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
//                startActivity(intent);
//            }
//        });

       // Log.d("**************", "onCreate: "+login);




    }




}
