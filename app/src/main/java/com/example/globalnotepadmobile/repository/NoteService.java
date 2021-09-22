package com.example.globalnotepadmobile.repository;

import com.example.globalnotepadmobile.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NoteService {

    @GET(Environment.ENDPOINT_LIST)
    Call<List<Note>> getNotes();

    @GET(Environment.ENDPOINT_SEARCH)
    Call<List<Note>> searchNote(@Query("title") String title);

    @POST(Environment.ENDPOINT_CREATE)
    Call<Note> createNote(@Body Note note);

    @PUT(Environment.ENDPOINT_UPDATE)
    Call<Note> updateNote(@Path("id") String id, @Body Note note);
}
