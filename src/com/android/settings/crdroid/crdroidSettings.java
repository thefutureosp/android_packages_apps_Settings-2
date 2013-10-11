package com.android.settings.crdroid;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class crdroidSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String UI_NOTIFICATION_BEHAVIOUR = "notifications_behaviour";
    private static final String CRDROID_CATEGORY = "crdroid_status"; 

    private ListPreference mNotificationsBehavior;
    private PreferenceCategory mCrdroidCategory; 

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.crdroid_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mContext = getActivity();

        int CurrentBehavior = Settings.System.getInt(getContentResolver(), Settings.System.NOTIFICATIONS_BEHAVIOUR, 0);
            mNotificationsBehavior = (ListPreference) findPreference(UI_NOTIFICATION_BEHAVIOUR);
            mNotificationsBehavior.setValue(String.valueOf(CurrentBehavior));
            mNotificationsBehavior.setSummary(mNotificationsBehavior.getEntry());
            mNotificationsBehavior.setOnPreferenceChangeListener(this);

	mCrdroidCategory = (PreferenceCategory) prefSet.findPreference(CRDROID_CATEGORY);  
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNotificationsBehavior) {
            String val = (String) newValue;
                     Settings.System.putInt(getContentResolver(), Settings.System.NOTIFICATIONS_BEHAVIOUR,
            Integer.valueOf(val));
            int index = mNotificationsBehavior.findIndexOfValue(val);
            mNotificationsBehavior.setSummary(mNotificationsBehavior.getEntries()[index]);
            return true;
	}  
        return false;
    }
}
