package com.nit.endterm;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNoteActivity extends AppCompatActivity {
    String date;
    SharedPreferences sp;
    String dateAndTime;
    private Note noteForEdit = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("login",MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Button btn = findViewById(R.id.btnsubmit);
        Button btnDatePicker = findViewById(R.id.btnDatePicker);
        TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
        EditText title = findViewById(R.id.etTitle);
        getSupportActionBar().setTitle("Add Note");
        Button btnTimepicker = findViewById(R.id.btnTimepicker);
        EditText etnote = findViewById(R.id.etNote);
        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.EXTRA_DETAILS)){

            Gson gson = new Gson();
             noteForEdit = gson.fromJson(getIntent().getStringExtra(MainActivity.EXTRA_DETAILS), Note.class);

            getSupportActionBar().setTitle("Edit Note");
            btn.setText("UPDATE");
            title.setText(noteForEdit.getTitle());
            tvSelectedDate.setText(noteForEdit.getEventTime());
            etnote.setText(noteForEdit.getDescription());



        }
        btnDatePicker.setOnClickListener(v -> {
            clickDatePicker(v,tvSelectedDate);

        });
        btnTimepicker.setOnClickListener(v -> {
            if(date!=null)
                clickTimePicker(v,tvSelectedDate);
            else
                Toast.makeText(this,"please select a date first",Toast.LENGTH_SHORT).show();

        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(noteForEdit!=null){
                           Note note = new Note(title.getText().toString(),etnote.getText().toString(),tvSelectedDate.getText().toString(),0);
                           Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:3000").addConverterFactory(
                                   GsonConverterFactory.create()
                           ).build();
                           RestApi RestApi = retrofit.create(RestApi.class);

                           Call<ResponseBody> call = RestApi.updatenote("Bearer "+sp.getString("Token",""),
                                  noteForEdit.getNote_id() ,note.getTitle(),note.getDescription(),note.getEventTime(),note.getIsDone());
                           call.enqueue(new Callback<ResponseBody>() {
                               @Override
                               public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                   if(response.isSuccessful()){
                                       Snackbar.make(v,"note updated",Snackbar.LENGTH_SHORT).show();
                                   }else{
                                       Snackbar.make(v,"sth went wrong try again",Snackbar.LENGTH_SHORT).show();
                                   }
                               }

                               @Override
                               public void onFailure(Call<ResponseBody> call, Throwable t) {
                                   Snackbar.make(v,"connection failed",Snackbar.LENGTH_SHORT).show();
                               }
                           });
                       }else{
                           if(dateAndTime==null){
                               Toast.makeText(v.getContext(),"please select a time first",Toast.LENGTH_SHORT).show();
                           }else{
                               Note note = new Note(title.getText().toString(),etnote.getText().toString(),dateAndTime,0);
                               Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:3000").addConverterFactory(
                                       GsonConverterFactory.create()
                               ).build();
                               RestApi RestApi = retrofit.create(RestApi.class);
                               Call<ResponseBody> call = RestApi.addnote("Bearer "+sp.getString("Token","")
                                       ,note.getTitle(),note.getDescription(),note.getEventTime(),note.getIsDone());
                               call.enqueue(new Callback<ResponseBody>() {
                                   @Override
                                   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                       if(response.isSuccessful()){
                                           Snackbar.make(v,"your note saved",Snackbar.LENGTH_SHORT).show();
                                       }else{
                                           Snackbar.make(v,"some thing went wrong",Snackbar.LENGTH_SHORT).show();
                                       }
                                   }

                                   @Override
                                   public void onFailure(Call<ResponseBody> call, Throwable t) {
                                       Snackbar.make(v,"some thing went wrong",Snackbar.LENGTH_SHORT).show();

                                   }
                               });
                           }
                       }
                    }
                }
        );

    }
    private void clickDatePicker(View view,TextView tvSelectedDate){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year1, month1, dayOfMonth) -> {

            String selectedDate = year1+"-"+(month1+1)+"-"+dayOfMonth;
            date=selectedDate;
            tvSelectedDate.setText(date);
           SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            try {
                Date date = sdf.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this,"sth went wrong",Toast.LENGTH_SHORT);
            }

        },year
        ,month,day);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();

    }
    private void clickTimePicker(View v,TextView tvSelectedDate){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute2 = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,(view, hourOfDay, minute) -> {
            String time = date+" "+hourOfDay+":"+minute;
            tvSelectedDate.setText(time);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
            dateAndTime=time;
            try {
                Date date = sdf.parse(time);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this,"sth went wrong",Toast.LENGTH_SHORT);
            }

        },hour,minute2,true);
        timePickerDialog.show();

    }


}