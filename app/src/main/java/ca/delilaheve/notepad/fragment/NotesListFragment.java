package ca.delilaheve.notepad.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import ca.delilaheve.notepad.MainActivity;
import ca.delilaheve.notepad.R;
import ca.delilaheve.notepad.adapter.CardAdapter;
import ca.delilaheve.notepad.data.Note;
import ca.delilaheve.notepad.data.Settings;
import ca.delilaheve.notepad.util.ColourPack;
import ca.delilaheve.notepad.util.NoteLoader;
import ca.delilaheve.notepad.util.Res;

public class NotesListFragment extends Fragment {

    private ArrayList<Note> notes;

    private CardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        // load notes
        notes = new NoteLoader().load().get();

        // set list adapter
        adapter = new CardAdapter(getActivity(), notes);
        ListView noteList = (ListView) view.findViewById(R.id.notes_list);
        noteList.setAdapter(adapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.instance.editNote(notes.get(position));
            }
        });
        noteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("");
                builder.setItems(R.array.note_options_listview, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // send
                                break;
                            case 1:
                                // details
                                MainActivity.instance.showNoteDetails(notes.get(position));
                                break;
                            case 2:
                                // delete
                                notes.get(position).delete();
                                refresh();
                                break;
                        }
                    }
                });
                builder.create().show();

                return true;
            }
        });

        // get primary colour
        Settings.Section s = MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_PRIMARY);
        ColourPack primary = new ColourPack(
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_RED).getValue()),
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_GREEN).getValue()),
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_BLUE).getValue())
        );

        // set button click
        ImageView addNote = (ImageView) view.findViewById(R.id.add_note_button);
        LayerDrawable drawable = ((LayerDrawable) addNote.getBackground());
        drawable.findDrawableByLayerId(R.id.circle).setColorFilter(Color.rgb(primary.red(), primary.green(), primary.blue()), PorterDuff.Mode.MULTIPLY);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.editNote(new Note("", ""));
            }
        });

        return view;
    }

    public void refresh(){
        notes = new NoteLoader().load().get();
        adapter.update(notes);
    }
}
