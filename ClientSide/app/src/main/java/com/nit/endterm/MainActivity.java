package com.nit.endterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    private Context context;
   public  static String EXTRA_DETAILS = "NOTE_DETAILS";
   public static int EDIT_NOTE =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,AddNoteActivity.class);
        sp = getSharedPreferences("login",MODE_PRIVATE);
        getNotes();
        FloatingActionButton fab = findViewById(R.id.fabInsert);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
    private  void getNotes(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:3000").addConverterFactory(
                GsonConverterFactory.create()
        ).build();
        RestApi RestApi = retrofit.create(RestApi.class);
        Call<ArrayList<Note>> call = RestApi.getNotes("Bearer "+sp.getString("Token",""));
        call.enqueue(new Callback<ArrayList<Note>>() {
            @Override
            public void onResponse(Call<ArrayList<Note>> call, Response<ArrayList<Note>> response) {
                if(response.isSuccessful()){
                   ArrayList<Note> notes = response.body();
                   if(notes.size()>0){
                       setupListofDataIntoRecyclerView(notes);
                   }




                }
            }

            @Override
            public void onFailure(Call<ArrayList<Note>> call, Throwable t) {

            }
        });


    }
    private void setupListofDataIntoRecyclerView(ArrayList<Note> list){

        // if(getNotes().size()>0){
        RecyclerView rvItemsList = findViewById(R.id.rvItemsList);
        rvItemsList.setLayoutManager(new LinearLayoutManager(this));
        rvItemsList.setAdapter(new AdapterForRecyclerView(this,list));
        SwipeToEditCallBack swipe=new SwipeToEditCallBack(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                   AdapterForRecyclerView adapter= (AdapterForRecyclerView) rvItemsList.getAdapter();
                   adapter.notifyEditItem((Activity) context, viewHolder.getAdapterPosition(),EDIT_NOTE);


            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe);
        itemTouchHelper.attachToRecyclerView(rvItemsList);
        //}
        SwipeToDeleteCallBack deleteCallBack = new SwipeToDeleteCallBack(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AdapterForRecyclerView adapter= (AdapterForRecyclerView) rvItemsList.getAdapter();
                adapter.notifyDeleteItem( findViewById(android.R.id.content).getRootView(), viewHolder.getAdapterPosition(),"Bearer "+sp.getString("Token",""));
            }

        };
        ItemTouchHelper itemTouchHelperDelete = new ItemTouchHelper(deleteCallBack);
        itemTouchHelperDelete.attachToRecyclerView(rvItemsList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("lol");
        getNotes();
    }
    public void  updateRecordDialog(Note  note) {
        Dialog updateDialog =new  Dialog(this, R.style.Theme_Dialog);
        updateDialog.setCancelable(false);
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.dialog_update);

        ((EditText) updateDialog.findViewById(R.id.etUpdateText)).setText(note.getDescription());
        ((EditText) updateDialog.findViewById(R.id.etUpdateTitle)).setText(note.getTitle());

       /* ((TextView) updateDialog.findViewById(R.id.tvUpdate)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = ((EditText) updateDialog.findViewById(R.id.etUpdateText)).getText().toString();
                        String text =((EditText) updateDialog.findViewById(R.id.etUpdateTitle)).getText().toString();
                        Database db = new Database(MainActivity.this);

                        if (!title.isEmpty() && !text.isEmpty()) {
                            long status =
                                    db.editNote(new Note(text, title, note.getNote_id()));
                            if (status > -1) {
                                Toast.makeText(MainActivity.this, "Record Updated.", Toast.LENGTH_LONG).show();

                                setupListofDataIntoRecyclerView();

                                updateDialog.dismiss(); // Dialog will be dismissed
                            }
                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    "title or text cannot be blank",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                    }
                });*/
        ((TextView) updateDialog.findViewById(R.id.tvCancel)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDialog.dismiss();
                    }
                })
        ;updateDialog.show();


        //Start the dialog and display it on screen.

    }
    public void deleteRecord(Note note){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("delete note");
        builder.setMessage("Are you sure you wants to delete \n"+note.getTitle());
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("yes",(dialog, which) -> {

            //calling the deleteEmployee method of DatabaseHandler class to delete record


            dialog.dismiss(); // Dialog will be dismissed
        } );
        builder.setNegativeButton("No",(dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

}