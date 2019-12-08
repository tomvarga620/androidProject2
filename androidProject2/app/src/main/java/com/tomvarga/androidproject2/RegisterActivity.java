package com.tomvarga.androidproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tomvarga.androidproject2.model.ResObj;
import com.tomvarga.androidproject2.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    UserService userService;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.regUsername);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        submit = findViewById(R.id.regBtnSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
            }
        });
    }

    private void reqRegistration
            (final String username, final String email,
             final String password, final int typeAccount){

        Call call = userService.registration(username,email,password,typeAccount);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    ResObj resObj = (ResObj) response.body();
                    if(resObj.getMessage().equals("true")){
                        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.wtf("WTF","Request not works");
            }
        });
    }
}
