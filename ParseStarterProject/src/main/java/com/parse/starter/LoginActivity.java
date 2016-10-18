package com.parse.starter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    RadioButton doc;
    RadioButton patient;

    SharedPreferences signupType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void signupActivity(View v){
        getSignupType();

    }

    private void getSignupType(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_signup_type);
        dialog.setTitle(R.string.activity_signup_type_title);
        dialog.setCancelable(false);

        doc = (RadioButton)dialog.findViewById(R.id.doc);
        patient = (RadioButton)dialog.findViewById(R.id.patient);

        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doc.isChecked()) {
                    signupType = LoginActivity.this.getSharedPreferences("com.parse.starter", Context.MODE_PRIVATE);
                    signupType.edit().putString("signupType","doc").apply();
                    Intent signupIntent = new Intent(getApplicationContext(), SignupActivity.class);
                    dialog.dismiss();
                    startActivity(signupIntent);
                }
            }
        });

        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(patient.isChecked()){
                    signupType = LoginActivity.this.getSharedPreferences("com.parse.starter", Context.MODE_PRIVATE);
                    signupType.edit().putString("signupType","patient").apply();
                    Intent signupIntent = new Intent(getApplicationContext(), SignupActivity.class);
                    dialog.dismiss();
                    startActivity(signupIntent);
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {

    }
}
