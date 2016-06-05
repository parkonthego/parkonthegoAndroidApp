package edu.scu.smurali.parkonthego.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.scu.smurali.parkonthego.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Forgot Password");
            actionBar.setIcon(R.mipmap.ic_park);
            //  actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            // actionBar.setHomeButtonEnabled(true);
        }
        catch(NullPointerException ex){
            Log.d("Forgot Password", "onCreate: Null pointer in action bar "+ex.getMessage());
        }

        submit = (Button) findViewById(R.id.forgotPasswordSubmitButton);
        email =  (EditText) findViewById(R.id.forgotPasswordEmail);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }



}
