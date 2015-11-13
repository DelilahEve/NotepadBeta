package ca.delilaheve.notepad.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import ca.delilaheve.notepad.MainActivity;
import ca.delilaheve.notepad.R;
import ca.delilaheve.notepad.data.Settings;
import ca.delilaheve.notepad.util.ColourPack;
import ca.delilaheve.notepad.util.Res;

public class NavigationDrawerFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        LinearLayout settingsButton, themeButton;

        settingsButton = (LinearLayout) v.findViewById(R.id.settings_button);
        themeButton = (LinearLayout) v.findViewById(R.id.theme_button);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.closeDrawer();
                // show settings dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final AlertDialog dialog;

                View wrapper = inflater.inflate(R.layout.dialog_wrapper, null, false);
                View settings = inflater.inflate(R.layout.fragment_settings, null, false);

                LinearLayout content = (LinearLayout) wrapper.findViewById(R.id.content);

                // set fragment
                content.addView(settings);

                TextView cancelButton, doneButton;
                cancelButton = (TextView) wrapper.findViewById(R.id.cancel_button);
                doneButton = (TextView) wrapper.findViewById(R.id.done_button);


                // get primary colour
                Settings.Section s = MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_PRIMARY);
                final ColourPack primary = new ColourPack(
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_RED).getValue()),
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_GREEN).getValue()),
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_BLUE).getValue())
                );
                LinearLayout buttons = (LinearLayout) wrapper.findViewById(R.id.bottom_buttons);
                buttons.setBackgroundColor(Color.rgb(primary.red(), primary.green(), primary.blue()));

                builder.setView(wrapper);
                dialog = builder.create();
                dialog.show();

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // save settings
                        dialog.dismiss();
                    }
                });
            }
        });
        themeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.closeDrawer();
                // show settings dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final AlertDialog dialog;

                View wrapper = inflater.inflate(R.layout.dialog_wrapper, null, false);
                final View settings = inflater.inflate(R.layout.fragment_theme_settings, null, false);

                final LinearLayout primary, secondary, accent;
                primary = (LinearLayout) settings.findViewById(R.id.primary);
                secondary = (LinearLayout) settings.findViewById(R.id.secondary);
                accent = (LinearLayout) settings.findViewById(R.id.accent);

                // get colour
                Settings.Section s = MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_PRIMARY);
                final ColourPack primaryColour = new ColourPack(
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_RED).getValue()),
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_GREEN).getValue()),
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_BLUE).getValue())
                );
                s = MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_SECONDARY);
                final ColourPack secondaryColour = new ColourPack(
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_RED).getValue()),
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_GREEN).getValue()),
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_BLUE).getValue())
                );
                s = MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_ACCENT);
                final ColourPack accentColour = new ColourPack(
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_RED).getValue()),
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_GREEN).getValue()),
                        Integer.parseInt((String)s.getPropertiy(Res.THEME_KEY_BLUE).getValue())
                );

                final LinearLayout content = (LinearLayout) wrapper.findViewById(R.id.content);

                View p, sec, a;
                p = settings.findViewById(R.id.primaryPreview);
                sec = settings.findViewById(R.id.secondaryPreview);
                a = settings.findViewById(R.id.accentPreview);

                p.setBackgroundColor(Color.rgb(primaryColour.red(), primaryColour.green(), primaryColour.blue()));
                sec.setBackgroundColor(Color.rgb(secondaryColour.red(), secondaryColour.green(), secondaryColour.blue()));
                a.setBackgroundColor(Color.rgb(accentColour.red(), accentColour.green(), accentColour.blue()));


                LinearLayout buttons = (LinearLayout) wrapper.findViewById(R.id.bottom_buttons);
                buttons.setBackgroundColor(Color.rgb(primaryColour.red(), primaryColour.green(), primaryColour.blue()));

                primary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // change view in linearlayout
                        content.removeAllViews();

                        final View view = View.inflate(getActivity(), R.layout.fragment_colour_picker, null);

                        final View colour = view.findViewById(R.id.colour_preview);
                        TextView colourName = (TextView) view.findViewById(R.id.colour_id);

                        colour.setBackgroundColor(Color.rgb(primaryColour.red(), primaryColour.green(), primaryColour.blue()));
                        colourName.setText(R.string.primary);

                        TextView cancel, save;
                        cancel = (TextView) view.findViewById(R.id.cancel_button);
                        save = (TextView) view.findViewById(R.id.save_button);

                        // Change Seekbars to reflect chosen colour
                        final SeekBar redBar, greenBar, blueBar;
                        redBar = (SeekBar) view.findViewById(R.id.red_bar);
                        greenBar = (SeekBar) view.findViewById(R.id.green_bar);
                        blueBar = (SeekBar) view.findViewById(R.id.blue_bar);

                        redBar.setProgress(primaryColour.red());
                        greenBar.setProgress(primaryColour.green());
                        blueBar.setProgress(primaryColour.blue());

                        // show hex value
                        EditText hex = (EditText) view.findViewById(R.id.hex_code);
                        hex.setText(String.format("#%s", primaryColour.asHex()));

                        // show rgb numbers
                        final EditText red, blue, green;
                        red = (EditText) view.findViewById(R.id.rgb_red);
                        green = (EditText) view.findViewById(R.id.rgb_green);
                        blue = (EditText) view.findViewById(R.id.rgb_blue);

                        red.setText(String.valueOf(primaryColour.red()));
                        green.setText(String.valueOf(primaryColour.green()));
                        blue.setText(String.valueOf(primaryColour.blue()));

                        class SeekChanged implements SeekBar.OnSeekBarChangeListener {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if(fromUser) {
                                    red.setText(String.valueOf(redBar.getProgress()));
                                    green.setText(String.valueOf(greenBar.getProgress()));
                                    blue.setText(String.valueOf(blueBar.getProgress()));
                                }
                                colour.setBackgroundColor(Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {}
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {}
                        }

                        redBar.setOnSeekBarChangeListener(new SeekChanged());
                        greenBar.setOnSeekBarChangeListener(new SeekChanged());
                        blueBar.setOnSeekBarChangeListener(new SeekChanged());

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                content.removeAllViews();
                                content.addView(settings);
                            }
                        });
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // save colour as ColourPack and add to Theme
                                Settings.Section section;
                                section = MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_PRIMARY);

                                EditText red, green, blue;
                                red = (EditText) view.findViewById(R.id.rgb_red);
                                green = (EditText) view.findViewById(R.id.rgb_green);
                                blue = (EditText) view.findViewById(R.id.rgb_blue);

                                String r, g, b;
                                r = red.getText().toString();
                                g = green.getText().toString();
                                b = blue.getText().toString();

                                section.setProperty(Res.THEME_KEY_RED, Integer.parseInt(r));
                                section.setProperty(Res.THEME_KEY_GREEN, Integer.parseInt(g));
                                section.setProperty(Res.THEME_KEY_BLUE, Integer.parseInt(b));
                                MainActivity.instance.themeSettings.save();

                                // go back to theme settings
                                content.removeAllViews();
                                content.addView(settings);
                            }
                        });

                        content.addView(view);
                    }
                });
                secondary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // change view in linearlayout
                        content.removeAllViews();
                        final View view = View.inflate(getActivity(), R.layout.fragment_colour_picker, null);

                        final View colour = view.findViewById(R.id.colour_preview);
                        TextView colourName = (TextView) view.findViewById(R.id.colour_id);

                        colour.setBackgroundColor(getResources().getColor(R.color.secondary));
                        colourName.setText(R.string.secondary);

                        TextView cancel, save;
                        cancel = (TextView) view.findViewById(R.id.cancel_button);
                        save = (TextView) view.findViewById(R.id.save_button);

                        // Change Seekbars to reflect chosen colour
                        final SeekBar redBar, greenBar, blueBar;
                        redBar = (SeekBar) view.findViewById(R.id.red_bar);
                        greenBar = (SeekBar) view.findViewById(R.id.green_bar);
                        blueBar = (SeekBar) view.findViewById(R.id.blue_bar);

                        redBar.setProgress(secondaryColour.red());
                        greenBar.setProgress(secondaryColour.green());
                        blueBar.setProgress(secondaryColour.blue());

                        // show hex value
                        EditText hex = (EditText) view.findViewById(R.id.hex_code);
                        hex.setText(String.format("#%s", secondaryColour.asHex()));

                        // show rgb numbers
                        final EditText red, blue, green;
                        red = (EditText) view.findViewById(R.id.rgb_red);
                        green = (EditText) view.findViewById(R.id.rgb_green);
                        blue = (EditText) view.findViewById(R.id.rgb_blue);

                        red.setText(String.valueOf(secondaryColour.red()));
                        green.setText(String.valueOf(secondaryColour.green()));
                        blue.setText(String.valueOf(secondaryColour.blue()));

                        class SeekChanged implements SeekBar.OnSeekBarChangeListener {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if(fromUser) {
                                    red.setText(String.valueOf(redBar.getProgress()));
                                    green.setText(String.valueOf(greenBar.getProgress()));
                                    blue.setText(String.valueOf(blueBar.getProgress()));
                                }
                                colour.setBackgroundColor(Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {}
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {}
                        }

                        redBar.setOnSeekBarChangeListener(new SeekChanged());
                        greenBar.setOnSeekBarChangeListener(new SeekChanged());
                        blueBar.setOnSeekBarChangeListener(new SeekChanged());

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                content.removeAllViews();
                                content.addView(settings);
                            }
                        });
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // save colour as ColourPack and add to Theme
                                Settings.Section section;
                                section = MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_SECONDARY);

                                EditText red, green, blue;
                                red = (EditText) view.findViewById(R.id.rgb_red);
                                green = (EditText) view.findViewById(R.id.rgb_green);
                                blue = (EditText) view.findViewById(R.id.rgb_blue);

                                String r, g, b;
                                r = red.getText().toString();
                                g = green.getText().toString();
                                b = blue.getText().toString();

                                section.setProperty(Res.THEME_KEY_RED, Integer.parseInt(r));
                                section.setProperty(Res.THEME_KEY_GREEN, Integer.parseInt(g));
                                section.setProperty(Res.THEME_KEY_BLUE, Integer.parseInt(b));
                                MainActivity.instance.themeSettings.save();

                                // go back to theme settings
                                content.removeAllViews();
                                content.addView(settings);
                            }
                        });

                        content.addView(view);
                    }
                });
                accent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // change view in linearlayout
                        content.removeAllViews();
                        final View view = View.inflate(getActivity(), R.layout.fragment_colour_picker, null);

                        final View colour = view.findViewById(R.id.colour_preview);
                        TextView colourName = (TextView) view.findViewById(R.id.colour_id);

                        colour.setBackgroundColor(getResources().getColor(R.color.accent));
                        colourName.setText(R.string.accent);

                        TextView cancel, save;
                        cancel = (TextView) view.findViewById(R.id.cancel_button);
                        save = (TextView) view.findViewById(R.id.save_button);

                        // Change Seekbars to reflect chosen colour
                        final SeekBar redBar, greenBar, blueBar;
                        redBar = (SeekBar) view.findViewById(R.id.red_bar);
                        greenBar = (SeekBar) view.findViewById(R.id.green_bar);
                        blueBar = (SeekBar) view.findViewById(R.id.blue_bar);

                        redBar.setProgress(accentColour.red());
                        greenBar.setProgress(accentColour.green());
                        blueBar.setProgress(accentColour.blue());

                        // show hex value
                        EditText hex = (EditText) view.findViewById(R.id.hex_code);
                        hex.setText(String.format("#%s", accentColour.asHex()));

                        // show rgb numbers
                        final EditText red, blue, green;
                        red = (EditText) view.findViewById(R.id.rgb_red);
                        green = (EditText) view.findViewById(R.id.rgb_green);
                        blue = (EditText) view.findViewById(R.id.rgb_blue);

                        red.setText(String.valueOf(accentColour.red()));
                        green.setText(String.valueOf(accentColour.green()));
                        blue.setText(String.valueOf(accentColour.blue()));

                        class SeekChanged implements SeekBar.OnSeekBarChangeListener {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if(fromUser) {
                                    red.setText(String.valueOf(redBar.getProgress()));
                                    green.setText(String.valueOf(greenBar.getProgress()));
                                    blue.setText(String.valueOf(blueBar.getProgress()));
                                }
                                colour.setBackgroundColor(Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {}
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {}
                        }

                        redBar.setOnSeekBarChangeListener(new SeekChanged());
                        greenBar.setOnSeekBarChangeListener(new SeekChanged());
                        blueBar.setOnSeekBarChangeListener(new SeekChanged());

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                content.removeAllViews();
                                content.addView(settings);
                            }
                        });
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // save colour as ColourPack and add to Theme
                                Settings.Section section;
                                section = MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_ACCENT);

                                EditText red, green, blue;
                                red = (EditText) view.findViewById(R.id.rgb_red);
                                green = (EditText) view.findViewById(R.id.rgb_green);
                                blue = (EditText) view.findViewById(R.id.rgb_blue);

                                String r, g, b;
                                r = red.getText().toString();
                                g = green.getText().toString();
                                b = blue.getText().toString();

                                section.setProperty(Res.THEME_KEY_RED, Integer.parseInt(r));
                                section.setProperty(Res.THEME_KEY_GREEN, Integer.parseInt(g));
                                section.setProperty(Res.THEME_KEY_BLUE, Integer.parseInt(b));
                                MainActivity.instance.themeSettings.save();

                                // go back to theme settings
                                content.removeAllViews();
                                content.addView(settings);
                            }
                        });

                        content.addView(view);
                    }
                });

                RadioButton light, dark;
                light = (RadioButton) settings.findViewById(R.id.light_theme);
                dark = (RadioButton) settings.findViewById(R.id.dark_theme);

                if(MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_INTERFACE).getPropertiy(Res.THEME_KEY_INTERFACE).getValue().equals(String.valueOf(Res.THEME_LIGHT)))
                    light.setChecked(true);
                else
                    dark.setChecked(true);

                light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Settings.Section section;
                        section = MainActivity.instance.themeSettings.getSection(Res.THEME_SECTION_INTERFACE);

                        int style = isChecked ? Res.THEME_LIGHT : Res.THEME_DARK;
                        section.setProperty(Res.THEME_KEY_INTERFACE, style);
                        MainActivity.instance.themeSettings.save();
                    }
                });

                content.addView(settings);

                TextView cancelButton, doneButton;
                cancelButton = (TextView) wrapper.findViewById(R.id.cancel_button);
                doneButton = (TextView) wrapper.findViewById(R.id.done_button);

                builder.setView(wrapper);
                dialog = builder.create();
                dialog.show();

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // apply theme later, do nothing here
                        dialog.dismiss();
                    }
                });
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        MainActivity.instance.reloadForTheme();
                    }
                });
            }
        });

        return v;
    }
}
