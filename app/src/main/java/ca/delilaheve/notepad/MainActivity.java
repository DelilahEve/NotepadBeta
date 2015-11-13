package ca.delilaheve.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import ca.delilaheve.notepad.data.Note;
import ca.delilaheve.notepad.data.Settings;
import ca.delilaheve.notepad.fragment.EditNoteFragment;
import ca.delilaheve.notepad.fragment.NotesListFragment;
import ca.delilaheve.notepad.util.ColourPack;
import ca.delilaheve.notepad.util.Res;

public class MainActivity extends Activity {

    // Instance:
    public static MainActivity instance;

    // Settings files
    public Settings settings;
    public Settings themeSettings;

    // Boolean
    public static Boolean isTabletMode = false;

    // Fragments
    private NotesListFragment notesList;

    // Navigation Drawer
    private DrawerLayout drawerLayout;
    private ScrollView drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new Settings(Res.SETTINGS_FILE_NAME);
        themeSettings = new Settings(Res.THEME_FILE_NAME);
        initTheme();
        setContentView(R.layout.activity_main);

        notesList = new NotesListFragment();

        // set up nav drawer toggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = (ScrollView) findViewById(R.id.left_drawer);

        // set up Toolbar as ActionBar
        setupToolbar();

        // add fragment(s) as appropriate
        getFragmentManager().beginTransaction().replace(R.id.main_content_frame, notesList, "list").commit();

        // important shit :o
        finalizeTheme();
        isTabletMode = getResources().getBoolean(R.bool.isTablet);
        instance = this;
    }

    private void setupToolbar() {
        ImageView menu, sort, search, save;

        menu = (ImageView) findViewById(R.id.action_drawer);
        sort = (ImageView) findViewById(R.id.action_sort);
        search = (ImageView) findViewById(R.id.action_search);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // notesList.setSortOrder(Order.MOST_RECENT)
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show search bar
            }
        });
    }

    public void openDrawer() {
        drawerLayout.openDrawer(drawer);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(drawer);
    }

    public void toggleDrawer() {
        if(drawerLayout != null)
            if(drawerLayout.isDrawerOpen(drawer))
                closeDrawer();
            else
                openDrawer();
    }

    public void editNote(Note note) {
        // make the fragment
        EditNoteFragment fragment = new EditNoteFragment();

        // set up to transition to EditNoteFragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Check for phone vs tablet
        int frameID = R.id.edit_content_frame;
        if(!isTabletMode){
            // is phone, add slide animation
            int slideIn, slideOut;
            slideIn = R.animator.slide_in;
            slideOut = R.animator.slide_out;
            transaction.setCustomAnimations(slideIn, slideOut, slideIn, slideOut);

            frameID = R.id.main_content_frame;
        }

        // add fragment
        transaction.replace(frameID, fragment, "editor");
        transaction.addToBackStack("add_editor");
        transaction.commit();

        // set note to be edited
        fragment.setNote(note);
    }

    public void refreshNoteList() {
        notesList.refresh();
    }

    public void showNoteDetails(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_note_details, null);

        TextView title, fileName, created, edited, size;
        title = (TextView) view.findViewById(R.id.note_title);
        fileName = (TextView) view.findViewById(R.id.note_file_name);
        created = (TextView) view.findViewById(R.id.note_created);
        edited = (TextView) view.findViewById(R.id.note_edited);
        size = (TextView) view.findViewById(R.id.note_file_size);

        title.setText(note.getTitle());
        fileName.setText(note.getFile().getName());
        created.setText(convertToDate(note.getCreated()));
        edited.setText(convertToDate(note.getEdited()));
        size.setText(String.valueOf(calcSize(note, false)));

        builder.setView(view);
        builder.create().show();
    }

    private String calcSize(Note note, Boolean si) {
        Long bytes = note.getFile().length();

        int unit = si ? 1000 : 1024;

        if (bytes < unit)
            return bytes + " B";

        int exp = (int) (Math.log(bytes) / Math.log(unit));

        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private String convertToDate(Long timestamp) {
        String dateString;

        dateString = String.valueOf(timestamp);

        String year, month, day, hour, minute, period;
        year = dateString.substring(0, 4);
        day = dateString.substring(6, 8);
        minute = dateString.substring(10, 12);

        String[] months = getResources().getStringArray(R.array.months);
        month = months[Integer.parseInt(dateString.substring(4, 6))-1];

        int h = Integer.parseInt(dateString.substring(8, 10));
        period = "AM";
        if(h > 12){
            h -= 12;
            period = "PM";
        }

        hour = String.valueOf(h);

        return hour + ":" + minute + " " + period + "  " + month + " " + day + ", " + year;
    }

    private void initTheme(){
        // Get ColourPack for primary & secondary colours
        ColourPack primary, secondary;

        Settings.Section s = themeSettings.getSection(Res.THEME_SECTION_PRIMARY);
        primary = new ColourPack(
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_RED).getValue()),
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_GREEN).getValue()),
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_BLUE).getValue())
                        );

        s = themeSettings.getSection(Res.THEME_SECTION_SECONDARY);
        secondary = new ColourPack(
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_RED).getValue()),
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_GREEN).getValue()),
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_BLUE).getValue())
        );

        // Pick Style from primary and interfaceStyle
        ColourPack textColour = primary.textColour();
        s = themeSettings.getSection(Res.THEME_SECTION_INTERFACE);
        int styleID = R.style.Theme_Notepad_Light_LightFeature;
        if(Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_INTERFACE).getValue()) == Res.THEME_LIGHT) {
            // light background
            if(textColour.red() == 0)
                styleID = R.style.Theme_Notepad_Light_DarkFeature;
        }
        else {
            // dark background
            if(textColour.red() == 0)
                styleID = R.style.Theme_Notepad_Dark_DarkFeature;
            else
                styleID = R.style.Theme_Notepad_Dark_LightFeature;
        }
        setTheme(styleID);

        // set nav & status colours if available
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setNavigationBarColor(Color.rgb(secondary.red(), secondary.green(), secondary.blue()));
            getWindow().setStatusBarColor(Color.rgb(secondary.red(), secondary.green(), secondary.blue()));
        }
    }

    public void finalizeTheme(){
        // Get ColourPacks for each colour
        ColourPack primary, accent;

        Settings.Section s = themeSettings.getSection(Res.THEME_SECTION_PRIMARY);
        primary = new ColourPack(
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_RED).getValue()),
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_GREEN).getValue()),
                Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_BLUE).getValue())
        );

        // Set toolbar background colour
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.rgb(primary.red(), primary.green(), primary.blue()));

        // set account background colour
        LinearLayout account = (LinearLayout) drawer.findViewById(R.id.account_container);
        account.setBackgroundColor(Color.rgb(primary.red(), primary.green(), primary.blue()));

        //? Action button background
        //? Account area background
        //? dialog wrapper buttons
    }

    public void reloadForTheme(){
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }
}
