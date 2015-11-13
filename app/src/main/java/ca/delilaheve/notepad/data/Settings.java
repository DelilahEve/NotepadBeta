package ca.delilaheve.notepad.data;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.delilaheve.notepad.util.Res;

public class Settings {

    private File file;
    private File folder = new File(Environment.getExternalStorageDirectory() + File.separator
            + Res.APP_CONTENT_FOLDER + File.separator + Res.SETTINGS_FOLDER_NAME);

    private ArrayList<Section> sections;

    public Settings(String fileName){
        if(!folder.exists())
            folder.mkdirs();

        file = new File(folder, fileName);
        if(!file.exists())
            if(fileName.equals(Res.SETTINGS_FILE_NAME))
                generate(0);
            else
                generate(1);
        load();
    }

    public void save(){
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // clear file
        writer.print("");

        // write data
        for(Section s : sections)
            s.write(writer);
        writer.close();
    }

    public void load(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));

            sections = new ArrayList<>();

            String line;
            Section s = null;
            while ((line = reader.readLine()) != null) {
                Matcher m = Pattern.compile("^(.+) \\{$").matcher(line);
                if(m.matches()) {
                    s = new Section(m.group(1));
                }
                else if(line.equals(Res.THEME_SECTION_CLOSE_CHAR)) {
                    if(s != null)
                        sections.add(s);

                    s = null;
                }
                else {
                    if(s != null) {
                        String[] pair = line.split("=");
                        s.setProperty(new Property(pair[0], pair[1]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Section getSection(String sectionName) {
        for(Section s : sections)
            if(s.getSectionTitle().equals(sectionName))
                return s;

        return null;
    }

    public void generate(int mode){
        sections = new ArrayList<>();

        switch (mode) {
            case 0:
                // settings file
                Section ads, sync;
                ads = new Section(Res.SETTING_SECTION_ADS);
                sync = new Section(Res.SETTING_SECTION_SYNC);

                ads.setProperty(new Property(Res.SETTING_KEY_ADS, Res.SETTING_TRUE));

                sync.setProperty(new Property(Res.SETTING_KEY_SYNC, Res.SETTING_TRUE));
                sync.setProperty(new Property(Res.SETTING_KEY_ACCOUNT, "none"));

                sections.add(ads);
                sections.add(sync);
                break;
            case 1:
                // theme file
                Section primary, secondary, accent, interfaceStyle;
                primary = new Section(Res.THEME_SECTION_PRIMARY);
                secondary = new Section(Res.THEME_SECTION_SECONDARY);
                accent = new Section(Res.THEME_SECTION_ACCENT);
                interfaceStyle = new Section(Res.THEME_SECTION_INTERFACE);

                primary.setProperty(new Property(Res.THEME_KEY_RED, Res.primary.red()));
                primary.setProperty(new Property(Res.THEME_KEY_GREEN, Res.primary.green()));
                primary.setProperty(new Property(Res.THEME_KEY_BLUE, Res.primary.blue()));

                secondary.setProperty(new Property(Res.THEME_KEY_RED, Res.secondary.red()));
                secondary.setProperty(new Property(Res.THEME_KEY_GREEN, Res.secondary.green()));
                secondary.setProperty(new Property(Res.THEME_KEY_BLUE, Res.secondary.blue()));

                accent.setProperty(new Property(Res.THEME_KEY_RED, Res.accent.red()));
                accent.setProperty(new Property(Res.THEME_KEY_GREEN, Res.accent.green()));
                accent.setProperty(new Property(Res.THEME_KEY_BLUE, Res.accent.blue()));

                interfaceStyle.setProperty(new Property(Res.THEME_KEY_INTERFACE, Res.THEME_LIGHT));

                sections.add(primary);
                sections.add(secondary);
                sections.add(accent);
                sections.add(interfaceStyle);
                break;
        }

        save();
    }

    public class Section {

        private String sectionTitle;
        private ArrayList<Property> properties;

        public Section(String sectionTitle){
            this.sectionTitle = sectionTitle;
            properties = new ArrayList<>();
        }

        public void write(PrintWriter writer) {
            writer.println(sectionTitle + " " + Res.THEME_SECTION_OPEN_CHAR);
            for(Property p : properties) {
                p.write(writer);
            }
            writer.println(Res.THEME_SECTION_CLOSE_CHAR);
        }

        public void setProperty(String key, Object value){
            setProperty(new Property(key, value));
        }

        public void setProperty(Property property) {
            int index = -1;
            for(Property p : properties)
                if(p.getKey().equals(property.getKey()))
                    index = properties.indexOf(p);

            if(index != -1)
                properties.remove(index);

            properties.add(property);
        }

        public String getSectionTitle() {
            return sectionTitle;
        }

        public Property getPropertiy(String key) {
            for(Property p : properties)
                if(p.getKey().equals(key))
                    return p;

            return null;
        }
    }

    public class Property {

        private String key;
        private Object value;

        public Property(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public void write(PrintWriter writer) {
            writer.println(key + "=" + value);
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }
}
