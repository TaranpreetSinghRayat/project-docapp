package com.parse.starter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SplashScreen extends AppCompatActivity {

    ImageView logo;
    ImageView logoImageError;
    TextView loadingText;

    Button cancelBtn;
    Button settingsBtn;

    Handler loadingHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        logo = (ImageView)findViewById(R.id.logoImage);
        loadingText = (TextView)findViewById(R.id.loadingText);


        createPulseEffect(logo);

        startLoading();
    }

    private void createPulseEffect(ImageView element){
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.anim_pulse);
        element.startAnimation(pulse);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void createNetworkError(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_network_error);
        dialog.setTitle(R.string.network_error_title);
        dialog.setCancelable(false);

        cancelBtn = (Button)dialog.findViewById(R.id.dialog_cancel);
        settingsBtn = (Button)dialog.findViewById(R.id.dialog_ok);
        logoImageError = (ImageView)dialog.findViewById(R.id.logoImageError);

        createPulseEffect(logoImageError);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(settingsIntent);
            }
        });
        dialog.show();
    }


    private void startLoading(){
        loadingHandler = new Handler();

        loadingHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if(isNetworkAvailable()){
                   loadingText.setText(loadingText.getText().toString() + " . ");

                   //2nd Handler
                   loadingHandler.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           loadingText.setText(loadingText.getText().toString() + " . ");
                       }
                   }, 2000);

                   //3rd Handler
                   loadingHandler.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           loadingText.setText(loadingText.getText().toString() + " . ");
                           Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                           finish();
                           startActivity(intent);
                       }
                   }, 1000);
               }else{
                   createNetworkError();
               }
            }
        },1000);
    }

}
