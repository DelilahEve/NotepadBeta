package ca.delilaheve.notepad.data;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.delilaheve.notepad.MainActivity;
import ca.delilaheve.notepad.util.Res;

public class Note {

    private File file;
    private File folder = new File(Environment.getExternalStorageDirectory() + File.separator
            + Res.APP_CONTENT_FOLDER + File.separator + Res.NOTES_FOLDER_NAME);

    private String title;
    private String text;

    private Long created;
    private Long edited;

    public Note(File file) {
        this.file = file;
        load();
    }

    public Note(String title, String text){
        this.title = title;
        this.text = text;

        // init file
        setDate();
        file = new File(folder, created.toString() + Res.FILE_NOTE_EXTENSION);
        if(!folder.exists())
            folder.mkdirs();
    }

    public void save(){
        edited();

        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // clear file
        writer.println("");

        // to store lines for file
        ArrayList<String> lines = new ArrayList<>();

        lines.add(Res.NoteTags.VERSION.openTag + 3.0 + Res.NoteTags.VERSION.closeTag);
        lines.add(Res.NoteTags.TITLE.openTag + title + Res.NoteTags.TITLE.closeTag );
        lines.add(Res.NoteTags.CREATED.openTag + String.valueOf(created) + Res.NoteTags.CREATED.closeTag);
        lines.add(Res.NoteTags.EDITED.openTag + String.valueOf(edited) + Res.NoteTags.EDITED.closeTag );
        lines.add(Res.NoteTags.TEXT.openTag);
        lines.add(text);
        lines.add(Res.NoteTags.TEXT.closeTag);

        for(String line : lines)
            writer.println(line);

        writer.close();

        MainActivity.instance.refreshNoteList();
    }

    public void load() {
        BufferedReader reader;
        try {

            reader = new BufferedReader(new FileReader(file));

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {

            String line;
            while((line = reader.readLine()) != null) {
                Pattern p = Pattern.compile("\\[([a-z]+)\\](.*)\\[/([a-z]+)\\]|\\[([a-z]+)\\]");
                Matcher matcher = p.matcher(line);

                if(matcher.matches()) {
                    String key, value;

                    key = matcher.group(1);
                    value = matcher.group(2);

                    if(key == null)
                        key = matcher.group(4);

                    switch (key){
                        case "title":
                            title = value;
                            break;
                        case "created":
                            created = Long.parseLong(value);
                            break;
                        case "edited":
                            edited = Long.parseLong(value);
                            break;
                        case "text":
                            String noteText = "";

                            while ((line = reader.readLine()) != null && !line.equalsIgnoreCase("[/text]"))
                                noteText += line + "\n";

                            text = noteText;
                            break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(){
        file.delete();
    }

    private void setDate() {
        created = getDate();
        edited = created;
    }

    private void edited() {
        edited = getDate();
    }

    private Long getDate(){
        return Long.parseLong(new SimpleDateFormat(Res.FILE_DATE_FORMAT).format(new Date()));
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public Long getCreated() {
        return created;
    }

    public Long getEdited() {
        return edited;
    }

    public File getFile() {
        return file;
    }
}
