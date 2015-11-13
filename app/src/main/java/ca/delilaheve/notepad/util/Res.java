package ca.delilaheve.notepad.util;

public class Res {

    // Tags that go in a note file
    public enum NoteTags {
        VERSION("file_version"),
        TITLE("title"),
        CREATED("created"),
        EDITED("edited"),
        TEXT("text");

        public String openTag;
        public String closeTag;

        NoteTags(String name) {
            openTag = "[" + name + "]";
            closeTag = "[/" + name + "]";
        }
    }

    // Default Colours as ColourPacks
    public static final ColourPack primary = new ColourPack("673AB7");
    public static final ColourPack secondary = new ColourPack("4A148C");
    public static final ColourPack accent = new ColourPack("AB47BC");

    // Folders for app data to go in
    public static final String APP_CONTENT_FOLDER = "Notepad";
    public static final String NOTES_FOLDER_NAME = "Notes";
    public static final String SETTINGS_FOLDER_NAME = "Settings";

    // Variables needed by Settings.Java
    // properties
    public static final String SETTINGS_FILE_NAME = "settings.dat";
    public static final String SETTING_KEY_ADS = "show_ads";
    public static final String SETTING_KEY_SYNC = "do_sync";
    public static final String SETTING_KEY_ACCOUNT = "sync_account";
    // sections
    public static final String SETTING_SECTION_ADS = "ads";
    public static final String SETTING_SECTION_SYNC = "sync";

    // Variables needed by Theme.Java
    public static final String THEME_FILE_NAME = "theme.dat";
    // properties
    public static final String THEME_KEY_RED = "r";
    public static final String THEME_KEY_GREEN = "g";
    public static final String THEME_KEY_BLUE = "b";
    public static final String THEME_KEY_INTERFACE = "style";
    // sections
    public static final String THEME_SECTION_PRIMARY = "primary";
    public static final String THEME_SECTION_SECONDARY = "secondary";
    public static final String THEME_SECTION_ACCENT = "accent";
    public static final String THEME_SECTION_INTERFACE = "interface";

    // Misc settings/theme file values/indicators
    public static final int SETTING_TRUE = 1;
    public static final int SETTING_FALSE = 0;
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final String THEME_SECTION_OPEN_CHAR = "{";
    public static final String THEME_SECTION_CLOSE_CHAR = "}";

    // note naming
    public static final String FILE_DATE_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String FILE_NOTE_EXTENSION = ".note";

    // for Decimal to Hexadecimal conversion
    public static final String HEX_NUMBERS = "0123456789ABCDEF";
}
