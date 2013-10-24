package com.android.settings.crdroid;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class HaloColors extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String KEY_HALO_COLORS = "halo_colors"; 
    private static final String KEY_HALO_CIRCLE_COLOR = "halo_circle_color";
    
    private CheckBoxPreference mHaloColors; 
    private ColorPickerPreference mHaloCircleColor;
    
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);    

        addPreferencesFromResource(R.xml.halo_colors_settings);
        PreferenceScreen prefSet = getPreferenceScreen();
        mContext = getActivity();

	mHaloColors = (CheckBoxPreference) prefSet.findPreference(KEY_HALO_COLORS);
        mHaloColors.setChecked(Settings.System.getInt(mContext.getContentResolver(),
            Settings.System.HALO_COLOR, 0) == 1);

        mHaloCircleColor = (ColorPickerPreference) prefSet.findPreference(KEY_HALO_CIRCLE_COLOR);
        mHaloCircleColor.setOnPreferenceChangeListener(this);
        int color = Settings.System.getInt(mContext.getContentResolver(), 
        		Settings.System.HALO_CIRCLE_COLOR, 0xff33b5b3);
        String hex = ColorPickerPreference.convertToARGB(color);
    	mHaloCircleColor.setSummary(hex);
    	mHaloCircleColor.setEnabled(isChecked());
    	
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	if (preference == mHaloColors) {
                Settings.System.putInt(mContext.getContentResolver(),
                                Settings.System.HALO_COLOR, mHaloColors.isChecked() ? 1 : 0);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHaloCircleColor) {
        	String hex = ColorPickerPreference.convertToARGB(
        			Integer.valueOf(String.valueOf(newValue)));
        	preference.setSummary(hex);
        	Settings.System.putInt(getActivity().getContentResolver(),
        			Settings.System.HALO_CIRCLE_COLOR, ColorPickerPreference.convertToColorInt(hex));
        }
        return false;
    }
}
