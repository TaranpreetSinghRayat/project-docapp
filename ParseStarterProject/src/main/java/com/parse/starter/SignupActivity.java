package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SignupActivity extends AppCompatActivity {

    TextView name;
    TextView address;
    TextView password;
    TextView email;
    TextView mobile;
    TextView confirmPassword;
    TextView docType;

    Button createAccount;

    SharedPreferences signupType;

    ParseUser userSystem;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (TextView)findViewById(R.id.input_name);
        address = (TextView)findViewById(R.id.input_address);
        password = (TextView)findViewById(R.id.input_password);
        confirmPassword = (TextView)findViewById(R.id.input_reEnterPassword);
        email = (TextView)findViewById(R.id.input_email);
        mobile = (TextView)findViewById(R.id.input_mobile);
        docType = (TextView)findViewById(R.id.input_typedoc);

        createAccount = (Button)findViewById(R.id.btn_signup);

        checkInputField();
    }

    private void checkInputField(){
        signupType = SignupActivity.this.getSharedPreferences("com.parse.starter", Context.MODE_PRIVATE);
        String type = signupType.getString("signupType", "");
        createPop(type);
        if(type == "doc"){
            docType.setVisibility(View.VISIBLE);
        }
    }

    public void loginActivity(View v){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(loginIntent);
    }

    public void createAccount(View v){
        if(validateInput() == true){
            createPop("Validation Passed !");
            signupDialog(0);
            userSystem = new ParseUser();
            userSystem.setUsername(name.getText().toString());
            userSystem.setPassword(password.getText().toString());
            userSystem.put("address",address.getText().toString());
            userSystem.setEmail(email.getText().toString());
            userSystem.put("mobile",mobile.getText().toString());
            userSystem.put("docType",docType.getText().toString());

            createNewDocType(docType.getText().toString());

            userSystem.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if(e == null){
                        signupDialog(1);
                        createPop("New user created successfully!");
                        loginActivity(findViewById(R.id.link_login));
                    }else{
                        signupDialog(1);
                        createPop(e.getMessage());
                    }
                }
            });
        }else{
            createPop("Error! Validation Fail");
        }
    }

    private static String[] sterilizationArray(String[] arr){
        List<String> list = Arrays.asList(arr);
        Set<String> set = new HashSet<String>(list);
        String[] result = new String[set.size()];
        set.toArray(result);
        return result;
    }


    private void createNewDocType(String docTypes){
        ParseObject docCategories = new ParseObject("DocCategories");
        docCategories.addUnique("cat",docTypes);
        docCategories.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if(e==null){
                    Log.i("Doc Categoris", "Save Success");
                }else{
                    Log.i("Error SaveDocCat", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void signupDialog(int status){
        if(status == 0){
            pDialog = new ProgressDialog(SignupActivity.this);
            pDialog.setMessage("Signing up user! Please wait");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }else if (status == 1){
            pDialog.dismiss();
        }
    }

    private boolean validateInput(){
        signupType = SignupActivity.this.getSharedPreferences("com.parse.starter", Context.MODE_PRIVATE);
        String type = signupType.getString("signupType", "");
        if(type == "doc"){
            if((name.getText().length() > 5)){
                if( (address.getText().length() > 1)){
                    if( (password.getText().length() > 5)){
                        if((password.getText().length() < 36)){
                            if((confirmPassword.getText().length() == password.getText().length())){
                                if((email.getText().length() > 1)){
                                   if((mobile.getText().length() > 5)){
                                       if((docType.getText().length() > 1)){
                                           return true;
                                       }else{
                                           return false;
                                       }
                                   }else{
                                       return false;
                                   }
                                }else{
                                    return false;
                                }
                            }else{
                                return false;
                            }
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private void createPop(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
