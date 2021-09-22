package com.example.globalnotepadmobile.presentation.noteform;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.globalnotepadmobile.model.Note;
import com.example.globalnotepadmobile.repository.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteFormViewModel extends ViewModel {
    private static final int STATUS_SUCCESS = 1;
    private static final int STATUS_ERROR = 2;

    private MutableLiveData<Integer> statusLiveData;

    public NoteFormViewModel(){
        statusLiveData = new MutableLiveData<>();
    }

    MutableLiveData<Integer> getStatusLiveData() {
        return statusLiveData;
    }

    void createNote(Note note) {
        ApiService.getInstance().getNoteService().createNote(note).enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                statusLiveData.setValue(STATUS_SUCCESS);
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                statusLiveData.setValue(STATUS_ERROR);
            }
        });
    }

    void updateNote(String id, Note note) {
        ApiService.getInstance().getNoteService().updateNote(id, note).enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                statusLiveData.setValue(STATUS_SUCCESS);
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                statusLiveData.setValue(STATUS_ERROR);
            }
        });
    }
}
