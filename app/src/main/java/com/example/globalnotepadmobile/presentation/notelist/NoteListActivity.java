package com.example.globalnotepadmobile.presentation.notelist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.globalnotepadmobile.R;
import com.example.globalnotepadmobile.model.Note;
import com.example.globalnotepadmobile.presentation.noteform.NoteFormActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private RecyclerView rvNotes;
    private ViewFlipper vfNoteList;
    private FloatingActionButton fab;
    private EditText etSearch;
    private Toolbar toolbar;
    private AdView adView;

    private NoteListViewModel viewModel;
    private NoteListAdapter adapter;
    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        doInitialSetup();

        setEventListeners();
        setViewModelObservers();

        viewModel.setNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.searchNote(etSearch.getText().toString());
    }

    private void doInitialSetup() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        rvNotes = findViewById(R.id.rvNotes);
        vfNoteList = findViewById(R.id.vfNoteList);
        etSearch = findViewById(R.id.etSearch);
        adView = findViewById(R.id.adView);
        btnRefresh = findViewById(R.id.btnRefresh);

        doAdSetup();
        viewModel = new ViewModelProvider(this).get(NoteListViewModel.class);
        adapter = new NoteListAdapter(new OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                if(!note.getPassword().isEmpty())
                    promptPassword(note);
                else
                    goToNoteForm(note);
            }
        });

        setSupportActionBar(toolbar);
        rvNotes.setLayoutManager(new LinearLayoutManager(NoteListActivity.this, LinearLayoutManager.VERTICAL, false));
        rvNotes.setAdapter(adapter);
    }

    private void doAdSetup() {
        MobileAds.initialize(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setEventListeners(){
        fab.setOnClickListener(view -> goToNoteForm(null));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.searchNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnRefresh.setOnClickListener(v -> {
            viewModel.setNotes();
        });
    }

    private void setViewModelObservers(){
        viewModel.getNotesLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });
        viewModel.getStatusLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                vfNoteList.setDisplayedChild(status);
            }
        });
    }

    private void promptPassword(final Note note) {
        final EditText etPromptPassword = new EditText(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.note_list_dialog_title);
        dialog.setView(etPromptPassword);
        dialog.setPositiveButton(R.string.note_list_dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(note.getPassword().equals(etPromptPassword.getText().toString()))
                    goToNoteForm(note);
                else
                    sendToast();
            }
        });
        dialog.show();
    }

    private void goToNoteForm(Note note) {
        Intent intent = new Intent(NoteListActivity.this, NoteFormActivity.class);
        intent.putExtra("NOTE", note);
        startActivity(intent);
    }

    private void sendToast(){
        Toast toast;
        String toastText = getString(R.string.note_list_invalid_password_toast);
        toast = Toast.makeText(NoteListActivity.this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }

    interface OnItemClickListener {
        void onItemClick(Note note);
    }

}