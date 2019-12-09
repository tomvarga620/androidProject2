package com.tomvarga.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.regUsername);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        btnSubmit = findViewById(R.id.regBtnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();

                regUser(name,mail,pass);

            }
        });
    }

    public void regUser(String name,String mail,String pass){

            RegistrationData regData = new RegistrationData();
            regData.setEmail(mail);
            regData.setPassword(pass);
            regData.setUsername(name);
            regData.setTypeAccount(1);

            Call<ResponseBody> call = RetroFitClient
                    .getInstance()
                    .getRegApi()
                    .regRequest(regData);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println(response.code());
                    Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.wtf("ERROR","Not Works");
                }
            });
    }

}


