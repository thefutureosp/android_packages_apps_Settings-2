/*
 * Copyright (C) 2013 The CyanogenMod project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cyanogenmod;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class ButtonSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String KEY_VOLUME_WAKE = "pref_volume_wake";
    private static final String KEY_VOLBTN_MUSIC_CTRL = "volbtn_music_controls";

    private static final String CATEGORY_VOLUME = "volume_keys";

    private CheckBoxPreference mVolumeWake;
    private CheckBoxPreference mVolBtnMusicCtrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.button_settings);

        final Resources res = getResources();
        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        final PreferenceCategory volumeCategory =
                (PreferenceCategory) prefScreen.findPreference(CATEGORY_VOLUME);

        mVolumeWake = (CheckBoxPreference) findPreference(KEY_VOLUME_WAKE);
        mVolBtnMusicCtrl = (CheckBoxPreference) findPreference(KEY_VOLBTN_MUSIC_CTRL);

        mVolBtnMusicCtrl.setChecked(Settings.System.getInt(resolver,
                Settings.System.VOLBTN_MUSIC_CONTROLS, 1) != 0);

        mVolumeWake.setChecked(Settings.System.getInt(resolver,
                Settings.System.VOLUME_WAKE_SCREEN, 0) == 1);
    }

    private void handleCheckboxClick(CheckBoxPreference pref, String setting) {
        Settings.System.putInt(getContentResolver(), setting, pref.isChecked() ? 1 : 0);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mVolumeWake) {
            handleCheckboxClick(mVolumeWake, Settings.System.VOLUME_WAKE_SCREEN);
            return true;
        } else if (preference == mVolBtnMusicCtrl) {
            handleCheckboxClick(mVolBtnMusicCtrl, Settings.System.VOLBTN_MUSIC_CONTROLS);
            return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
