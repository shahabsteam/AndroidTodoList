package com.nit.endterm;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
public interface RestApi {

    @POST("/api/login")
    Call<ResponseBody> login(@Query("username")String username, @Query("password")String password);
    @POST("/api/register")
    Call<ResponseBody> register(@Query("username")String username, @Query("password")String password,
                                @Query("email")String email);
    @GET("/api/getnotes")
    Call<ArrayList<Note>>getNotes(@Header("Authorization")String token);
    @PUT("/api/addnote")
    Call<ResponseBody>addnote(@Header("Authorization")String token,
                                 @Query("title")String title, @Query("description")String description,
                                   @Query("eventTime")String eventTime, @Query("isDone")int isDone );
    @PUT("/api/updatenote")
    Call<ResponseBody>updatenote(@Header("Authorization")String token, @Query("id")String noteId,
                              @Query("title")String title, @Query("description")String description,
                              @Query("eventTime")String eventTime, @Query("isDone")int isDone );
    @DELETE("/api/deletenote")
    Call<ResponseBody>deletenote(@Header("Authorization")String token,
                              @Query("id")String noteId );
    @PUT("/api/isdone")
    Call<ResponseBody>isDone(@Header("Authorization")String token, @Query("isDone")int isDone , @Query("id")String noteId );

}
