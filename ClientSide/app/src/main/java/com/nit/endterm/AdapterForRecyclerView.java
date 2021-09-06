package com.nit.endterm;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.Context.MODE_PRIVATE;

public class AdapterForRecyclerView extends RecyclerView.Adapter<AdapterForRecyclerView.ViewHolder> {

    Context context;
    ArrayList<Note> list;
    LayoutInflater layoutInflater;
    AdapterForRecyclerView(Context context,ArrayList<Note> list ){
        this.list=list;

        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);//در مواردی که شما می خواهید درون یک view، از یک فایل XML یک شئ UI بسازید (رندر کنید)، LayoutInflater این کار را برای شما انجام می دهد.
    }
    @NonNull
    @Override
    public AdapterForRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.items_row,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForRecyclerView.ViewHolder holder, int position) {
        Note note = list.get(position);


        holder.textViewTitle.setText(note.getTitle());
        holder.textViewText.setText(note.getDescription());
        holder.tvDate.setText(note.getEventTime());
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                    ContextCompat.getColor(
                            context,
                            R.color.colorLightGray
                    )
            );
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        }
        if(note.getIsDone()==1){
            holder.cbIsDone.setChecked(true);
        }
        holder.cbIsDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                note.setIsDone(1);
            }else{
                note.setIsDone(0);
            }
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:3000").addConverterFactory(
                    GsonConverterFactory.create()
            ).build();
            RestApi RestApi = retrofit.create(RestApi.class);

            Call<ResponseBody> call = RestApi.isDone("Bearer "+holder.pref.getString("Token",""),note.getIsDone(),note.getNote_id());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }
    public void notifyEditItem(Activity activity,int position,int requestCode){
        Intent intent = new Intent(context,AddNoteActivity.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(list.get(position));
        intent.putExtra(MainActivity.EXTRA_DETAILS, myJson);
        activity.startActivityForResult(intent,requestCode);
        notifyItemChanged(position);

    }
    public void notifyDeleteItem(View activity,int position,String token){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:3000").addConverterFactory(
                GsonConverterFactory.create()
        ).build();
        RestApi RestApi = retrofit.create(RestApi.class);
        Call<ResponseBody> call = RestApi.deletenote(token,list.get(position).getNote_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    list.remove(position);
                    Snackbar.make(activity,"note deleted",Snackbar.LENGTH_SHORT).show();
                   // notifyItemChanged(position);
                    notifyItemRemoved(position);
                }else{
                    Snackbar.make(activity,"some thing went wrong",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snackbar.make(activity,"some thing went wrong",Snackbar.LENGTH_SHORT).show();
            }
        });



    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        CheckBox cbIsDone;
        TextView textViewText;
        TextView tvDate;
        LinearLayout llMain;
        SharedPreferences pref;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             pref = context.getSharedPreferences("login",MODE_PRIVATE);

            textViewTitle = itemView.findViewById(R.id.tvTitle);
            llMain=itemView.findViewById(R.id.llMain);
            cbIsDone=itemView.findViewById(R.id.cb_isDone);
            textViewText= itemView.findViewById(R.id.textViewText);
            tvDate=itemView.findViewById(R.id.tv_Date);


        }
    }
}

