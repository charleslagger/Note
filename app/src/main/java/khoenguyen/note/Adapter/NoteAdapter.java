package khoenguyen.note.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import khoenguyen.note.NoteActivity;
import khoenguyen.note.R;
import khoenguyen.note.enums.NoteType;
import khoenguyen.note.module.Note;

/**
 * Created by Admin on 5/3/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> noteList;
    private Activity activity ;
    public NoteAdapter(ArrayList<Note> noteList,Activity activity) {
        this.activity = activity;
        this.noteList = noteList;
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int i) {
        final Note note = noteList.get(i);
        noteViewHolder.title.setText(note.getTitle());
        noteViewHolder.note.setText(note.getContent());
        noteViewHolder.date_time.setText(note.getCreateDate());
        //noteViewHolder.cv.setCardBackgroundColor(note.getColor());

        //bat su kien: vao man hinh sua
        noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, NoteActivity.class) ;
                i.putExtra("edit",note) ;
                activity.startActivityForResult(i,NoteType.NOTE_EDIT_REQUEST);
            }
        });
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);

        return new NoteViewHolder(itemView);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView note;
        protected TextView date_time;
        protected CardView cv;
        private View view ;
        public NoteViewHolder(View v) {
            super(v);
            view =  v ;
            title =  (TextView) v.findViewById(R.id.cardview_note_title);
            note = (TextView) v.findViewById(R.id.note_item1);
            date_time = (TextView) v.findViewById(R.id.note_item4);
            cv = (CardView) v.findViewById(R.id.card);
        }
    }
}
