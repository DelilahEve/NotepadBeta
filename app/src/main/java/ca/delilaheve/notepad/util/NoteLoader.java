package ca.delilaheve.notepad.util;

import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import ca.delilaheve.notepad.data.Note;

public class NoteLoader {

    private File folder;
    private File[] noteFiles;

    private ArrayList<Note> notes;

    public NoteLoader() {
        folder = new File(Environment.getExternalStorageDirectory() + File.separator
                + Res.APP_CONTENT_FOLDER + File.separator + Res.NOTES_FOLDER_NAME);

        notes = new ArrayList<>();
    }

    public NoteLoader load(){
        if(!folder.exists())
            return this;

        // load all notes from folder
        noteFiles = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(Res.FILE_NOTE_EXTENSION);
            }
        });

        for(File file : noteFiles)
            notes.add(new Note(file));

        return this;
    }

    public ArrayList<Note> get(){
        return notes;
    }
}
