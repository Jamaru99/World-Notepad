package com.example.globalnotepadmobile.repository;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static ApiService instance;
    private NoteService noteService;

    public static ApiService getInstance(){
        if(instance == null)
            instance = new ApiService();
        return instance;
    }

    public NoteService getNoteService() {
        return noteService;
    }

    private ApiService() {
        noteService = initRetrofit().create(NoteService.class);
    }

    private Retrofit initRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Environment.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
