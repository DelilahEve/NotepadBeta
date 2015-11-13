package ca.delilaheve.notepad.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import ca.delilaheve.notepad.MainActivity;
import ca.delilaheve.notepad.R;
import ca.delilaheve.notepad.adapter.SimpleListAdapter;
import ca.delilaheve.notepad.data.Note;

public class EditNoteFragment extends Fragment {

    private Note current;

    private View layout;

    private Boolean ready = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_edit_note, container, false);

        ImageView noteOptions = (ImageView) layout.findViewById(R.id.note_options_button);
        SimpleListAdapter adapter = new SimpleListAdapter(getActivity(), R.array.note_options);
        final ListPopupWindow popupWindow = new ListPopupWindow(getActivity());
        popupWindow.setAdapter(adapter);
        popupWindow.setAnchorView(noteOptions);
        popupWindow.setHorizontalOffset(-100);

        float width, height;
        width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 176, getResources().getDisplayMetrics());
        popupWindow.setWidth((int) width);
        popupWindow.setHeight((int) height);
        popupWindow.setModal(true);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // send
                        break;
                    case 1:
                        // note details
                        MainActivity.instance.showNoteDetails(current);
                        break;
                    case 2:
                        getFragmentManager().popBackStack();
                        popupWindow.dismiss();
                        break;
                    case 3:
                        // delete
                        current.delete();
                        getFragmentManager().popBackStack();
                        popupWindow.dismiss();
                        MainActivity.instance.refreshNoteList();
                        break;
                }
            }
        });

        noteOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.show();
            }
        });

        final EditText title, text;
        title = (EditText) layout.findViewById(R.id.note_title);
        text = (EditText) layout.findViewById(R.id.note_text);

        class Watcher implements TextWatcher {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(current == null || isEmptyNote() || !ready)
                    return;

                current.setTitle(title.getText().toString());
                current.setText(text.getText().toString());
                current.save();
            }
        }
        title.addTextChangedListener(new Watcher());
        text.addTextChangedListener(new Watcher());

        update();
        ready = true;
        return layout;
    }

    public void setNote(Note current) {
        this.current = current;
        update();
    }

    public void update() {
        if (layout == null || current == null)
            return;

        TextView title, text;
        title = (TextView) layout.findViewById(R.id.note_title);
        text = (TextView) layout.findViewById(R.id.note_text);

        title.setText(current.getTitle());
        text.setText(current.getText());
    }

    private boolean isEmptyNote(){
        if (layout == null || current == null)
            return true;

        TextView title, text;
        title = (TextView) layout.findViewById(R.id.note_title);
        text = (TextView) layout.findViewById(R.id.note_text);

        return current.getText().equals("")
                && current.getTitle().equals("")
                && text.getText().toString().equals("")
                && title.getText().toString().equals("");
    }
}
