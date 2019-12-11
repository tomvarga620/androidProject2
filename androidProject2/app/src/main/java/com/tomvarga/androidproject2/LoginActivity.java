package com.tomvarga.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.tomvarga.androidproject2.UserData.LoginData;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView signUp;
    EditText username,password;
    Button btnSubmit;

    AwesomeValidation validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.loginUsername);
        password = (EditText) findViewById(R.id.loginPassword);

        validator = new AwesomeValidation(ValidationStyle.BASIC);

        //username
        validator.addValidation(this,R.id.loginUsername, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        //password
        validator.addValidation(this,R.id.loginPassword, RegexTemplate.NOT_EMPTY,R.string.invalid_pass);

        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        btnSubmit = findViewById(R.id.loginBtnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String name = username.getText().toString();
                 String pass = password.getText().toString();
                 if(validator.validate()){
                     loginUser(name,pass);
                 }else {
                     Toast.makeText(getApplicationContext(),"Login Error",Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }

    public void loginUser(final String name,String pass){

        LoginData data = new LoginData(name,pass);

        Call<ResponseBody> call = RetroFitClient
                .getInstance()
                .getLoginApi()
                .logRequest(data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    String token = null;
                    try {
                        token = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    setTokenToPrefs(token);
                    setUsernameToPrefs(name);

                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setTokenToPrefs(String token){
        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.apply();
    }

    private void setUsernameToPrefs(String name){
        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        editor.putString("username", name);
        editor.apply();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_out_left);
    }
}
