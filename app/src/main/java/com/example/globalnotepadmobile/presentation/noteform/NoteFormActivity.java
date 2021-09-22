package com.example.globalnotepadmobile.presentation.noteform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.globalnotepadmobile.R;
import com.example.globalnotepadmobile.model.Note;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class NoteFormActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;
    private EditText etPassword;
    private CheckBox cbPassword;
    private Button btnSave;
    private ViewFlipper vfLoadingButton;
    private AdView adView;

    private NoteFormViewModel viewModel;
    private Note note;

    private boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_form);

        doInitialSetup();
        setEventListeners();
        setViewModelObservers();

        if(isEditing)
            populateForm();
    }

    private void doInitialSetup() {
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        etPassword = findViewById(R.id.etPassword);
        cbPassword = findViewById(R.id.cbPassword);
        btnSave = findViewById(R.id.btnSave);
        vfLoadingButton = findViewById(R.id.vfLoadingButton);
        adView = findViewById(R.id.adView);

        doAdSetup();
        viewModel = new ViewModelProvider(this).get(NoteFormViewModel.class);

        note = (Note) getIntent().getExtras().getSerializable("NOTE");
        isEditing = note != null;
    }

    private void doAdSetup() {
        MobileAds.initialize(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setEventListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etPassword.setEnabled(isChecked);
            }
        });
    }

    private void setViewModelObservers() {
        viewModel.getStatusLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                vfLoadingButton.setDisplayedChild(0);
                sendToast(status);
                if(!isEditing) finish();
            }
        });
    }

    private void populateForm() {
        etTitle.setText(note.getTitle());
        etContent.setText(note.getContent());
        boolean hasPassword = !note.getPassword().isEmpty();
        if(hasPassword) {
            etPassword.setText(note.getPassword());
            cbPassword.setChecked(true);
        }
    }

    private void submitForm() {
        vfLoadingButton.setDisplayedChild(1);
        String title = !etTitle.getText().toString().isEmpty() ? etTitle.getText().toString() : "untitled";
        String content = etContent.getText().toString();
        String password = cbPassword.isChecked() ? etPassword.getText().toString() : "";

        Note newNote = new Note(null, title, content, password);

        if(!isEditing)
            viewModel.createNote(newNote);
        else
            viewModel.updateNote(note.getId(), newNote);
    }

    private void sendToast(int status){
        Toast toast;
        String toastText;
        if(status == 1)
            toastText = getString(R.string.note_form_success_toast);
        else
            toastText = getString(R.string.unexpected_error);
        toast = Toast.makeText(NoteFormActivity.this, toastText, Toast.LENGTH_SHORT);
        toast.show();
    }
}
