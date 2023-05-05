package com.example.globalnotepadmobile.presentation.notelist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.globalnotepadmobile.model.Note;
import com.example.globalnotepadmobile.repository.ApiService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteListViewModel extends ViewModel {

    private static final int STATUS_LOADING = 0;
    private static final int STATUS_SUCCESS_WITH_RESULTS = 1;
    private static final int STATUS_SUCCESS_WITH_NO_RESULTS = 2;
    private static final int STATUS_ERROR = 3;
    private static final int HTTP_OK = 200;

    private MutableLiveData<List<Note>> notesLiveData;
    private MutableLiveData<Integer> statusLiveData;

    public NoteListViewModel(){
        notesLiveData = new MutableLiveData<>();
        statusLiveData = new MutableLiveData<>();
    }

    MutableLiveData<List<Note>> getNotesLiveData() {
        return notesLiveData;
    }

    MutableLiveData<Integer> getStatusLiveData() {
        return statusLiveData;
    }

    void setNotes() {
        statusLiveData.setValue(STATUS_LOADING);
        ApiService.getInstance().getNoteService().getNotes().enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (response.code() == HTTP_OK) {
                    notesLiveData.setValue(response.body());
                    statusLiveData.setValue(STATUS_SUCCESS_WITH_RESULTS);
                } else {
                    statusLiveData.setValue(STATUS_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                String timeoutMessage = "timeout";
                if (Objects.equals(t.getMessage(), timeoutMessage)) {
                    setNotes();
                } else {
                    statusLiveData.setValue(STATUS_ERROR);
                }
            }
        });
    }

    void searchNote(String title) {
        ApiService.getInstance().getNoteService().searchNote(title).enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                List<Note> responseBody = response.body();
                notesLiveData.setValue(responseBody);
                if(responseBody != null && responseBody.size() > 0)
                    statusLiveData.setValue(STATUS_SUCCESS_WITH_RESULTS);
                else
                    statusLiveData.setValue(STATUS_SUCCESS_WITH_NO_RESULTS);
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                statusLiveData.setValue(STATUS_ERROR);
            }
        });
    }
}
