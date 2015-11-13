package ca.delilaheve.notepad.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ca.delilaheve.notepad.R;
import ca.delilaheve.notepad.data.Note;

public class CardAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Note> notes;

    public CardAdapter(Context context, ArrayList<Note> notes){
        this.context = context;
        this.notes = notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.card_note, null);

        TextView title, text;
        title = (TextView) view.findViewById(R.id.note_title);
        text = (TextView) view.findViewById(R.id.note_text);

        title.setText(notes.get(position).getTitle());
        text.setText(notes.get(position).getText());

        return view;
    }

    @Override
    public Note getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    public void update(ArrayList<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
}
