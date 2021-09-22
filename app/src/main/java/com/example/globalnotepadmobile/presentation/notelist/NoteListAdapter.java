package com.example.globalnotepadmobile.presentation.notelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globalnotepadmobile.R;
import com.example.globalnotepadmobile.model.Note;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder> {

    private List<Note> notes;
    private NoteListActivity.OnItemClickListener onItemClickListener;

    NoteListAdapter(NoteListActivity.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, parent, false);
        return new NoteListViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.NoteListViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    class NoteListViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNoteTitle;
        private ConstraintLayout noteListItemContainer;
        private NoteListActivity.OnItemClickListener onItemClickListener;
        private View vLockIcon;

        NoteListViewHolder(@NonNull View itemView, NoteListActivity.OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
            noteListItemContainer = itemView.findViewById(R.id.noteListItemContainer);
            vLockIcon = itemView.findViewById(R.id.vLockIcon);
        }

        void bind(final Note note) {
            tvNoteTitle.setText(note.getTitle());
            noteListItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(note);
                }
            });

            boolean hasPassword = !note.getPassword().isEmpty();
            if (hasPassword)
                vLockIcon.setVisibility(View.VISIBLE);
            else
                vLockIcon.setVisibility(View.INVISIBLE);
        }
    }
}
