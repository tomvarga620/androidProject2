package com.tomvarga.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.tomvarga.androidproject2.UserData.RegistrationData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText confirmPass;
    Button btnSubmit;

    AwesomeValidation validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.regUsername);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        confirmPass = findViewById(R.id.regPasswordRepeat);
        btnSubmit = findViewById(R.id.regBtnSubmit);

        validator = new AwesomeValidation(ValidationStyle.BASIC);

        validator.addValidation(this,R.id.regUsername,RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        validator.addValidation(this,R.id.regEmail, Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        validator.addValidation(this,R.id.regPassword,RegexTemplate.NOT_EMPTY,R.string.invalid_pass);
        validator.addValidation(this,R.id.regPasswordRepeat,R.id.regPassword,R.string.invalid_pass);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validator.validate()){
                    String name = username.getText().toString();
                    String mail = email.getText().toString();
                    String pass = password.getText().toString();
                    regUser(name,mail,pass);
                }else {
                    Toast.makeText(getApplicationContext(),"Registration Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void regUser(String name,String mail,String pass){

            RegistrationData regData = new RegistrationData(mail,pass,name,1);

            Call<ResponseBody> call = RetroFitClient
                    .getInstance()
                    .getRegApi()
                    .regRequest(regData);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println(response.code());
                    Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.wtf("ERROR","Not Works");
                }
            });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_out_left);
    }


}


