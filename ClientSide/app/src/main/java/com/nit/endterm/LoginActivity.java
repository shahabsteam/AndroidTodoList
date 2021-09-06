package com.nit.endterm;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("login",MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tvSignUp= findViewById(R.id.tv_signup);
        TextView tvusername= findViewById(R.id.et_name);
        TextView tvpassword= findViewById(R.id.et_password);
        Button login = findViewById(R.id.btn_login);
        Button register = findViewById(R.id.btn_register);
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:3000").addConverterFactory(
                                GsonConverterFactory.create()
                        ).build();
                        RestApi RestApi = retrofit.create(RestApi.class);
                        Call<ResponseBody> call = RestApi.login(tvusername.getText().toString(),tvpassword.getText().toString());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()){
                                    try {
                                        JSONObject myObject = new JSONObject(response.body().string());
                                        System.out.println(myObject.get("token"));
                                        sp.edit().putBoolean("logged",true).apply();
                                        sp.edit().putString("Token",myObject.get("token").toString()).apply();
                                        goToMainActivity();
                                        finish();
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Snackbar.make(v,"wrong username and password",Snackbar.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println("failure");
                                System.out.println(t);
                                Snackbar.make(v,"something went wrong try again",Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }});
        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this,RegisterActivity.class));
            finish();
        });

    }
    public void goToMainActivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}