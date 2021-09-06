package com.nit.endterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView tvLogin = findViewById(R.id.tv_login);
        EditText username = findViewById(R.id.et_name);
        EditText password = findViewById(R.id.et_password);
        EditText email = findViewById(R.id.et_email);
        Button btn_register = findViewById(R.id.btn_register);
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        });
        btn_register.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:3000").addConverterFactory(
                    GsonConverterFactory.create()
            ).build();
            RestApi RestApi = retrofit.create(RestApi.class);
            Call<ResponseBody> call = RestApi.register(username.getText().toString(),password.getText().toString(),email.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        try {
                            JSONObject myObject = new JSONObject(response.body().string());
                            Snackbar.make(v,myObject.get("message").toString(),Snackbar.LENGTH_LONG).show();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Snackbar.make(v,"server failure",Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("failure");
                    System.out.println(t);
                    Snackbar.make(v,"something went wrong try again",Snackbar.LENGTH_LONG).show();
                }
            });
        });
    }
}