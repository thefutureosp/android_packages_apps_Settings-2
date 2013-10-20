package com.android.settings.crdroid;

import android.app.AlertDialog; 
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;   
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.Spannable; 
import android.widget.EditText;    

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import java.util.ArrayList;

public class crdroidSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String UI_NOTIFICATION_BEHAVIOUR = "notifications_behaviour";
    private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";
    private static final String PREF_CUSTOM_CARRIER_LABEL = "custom_carrier_label";
    private static final String KEY_WAKEUP_WHEN_PLUGGED_UNPLUGGED = "wakeup_when_plugged_unplugged";
    private static final String KEY_LOW_BATTERY_WARNING_POLICY = "pref_low_battery_warning_policy";
    private static final String KEY_NAVIGATION_RING = "navigation_ring";
    private static final String KEY_RECENTS_RAM_BAR = "recents_ram_bar";   
    private static final String PREF_USE_ALT_RESOLVER = "use_alt_resolver";
    private static final String PREF_NOTIFICATION_SHOW_WIFI_SSID = "notification_show_wifi_ssid";
    private static final String PREF_FLIP_QS_TILES = "flip_qs_tiles";
    private static final String NO_NOTIFICATIONS_PULLDOWN = "no_notifications_pulldown";
    private static final String KEY_SCREEN_ON_NOTIFICATION_LED = "screen_on_notification_led";           
    private static final String PREF_STATUS_BAR_QUICK_PEEK = "status_bar_quick_peek";
    private static final String PREF_STATUS_BAR_TRAFFIC_ENABLE = "status_bar_traffic_enable";
    private static final String PREF_STATUS_BAR_TRAFFIC_HIDE = "status_bar_traffic_hide";
    private static final String STATUS_BAR_TRAFFIC_SUMMARY = "status_bar_traffic_summary";
    private static final String MEDIA_SCANNER_ON_BOOT = "media_scanner_on_boot"; 
    private static final String UI_COLLAPSE_BEHAVIOUR = "notification_drawer_collapse_on_dismiss";
    private static final String PREF_LESS_NOTIFICATION_SOUNDS = "less_notification_sounds";     
    private static final String CRDROID_CATEGORY = "crdroid_status"; 

    private PreferenceCategory mCrdroidCategory; 
    private ListPreference mNotificationsBehavior;
    private CheckBoxPreference mKillAppLongpressBack;
    private Preference mCustomLabel;
    private CheckBoxPreference mWakeUpWhenPluggedOrUnplugged;
    private ListPreference mLowBatteryWarning;
    private Preference mRamBar;    
    private CheckBoxPreference mUseAltResolver;
    private CheckBoxPreference mShowWifiName;
    private CheckBoxPreference mFlipQsTiles;
    private ListPreference mNoNotificationsPulldown;
    private CheckBoxPreference mScreenOnNotificationLed;     
    private CheckBoxPreference mStatusBarQuickPeek; 
    private CheckBoxPreference mStatusBarTrafficEnable;
    private CheckBoxPreference mStatusBarTrafficHide;
    private ListPreference mStatusBarTrafficSummary;
    private ListPreference mMSOB; 
    private ListPreference mCollapseOnDismiss;
    private ListPreference mAnnoyingNotifications;     

    private String mCustomLabelText = null;  
 
    private Context mContext;

    private final ArrayList<Preference> mAllPrefs = new ArrayList<Preference>();
    private final ArrayList<CheckBoxPreference> mResetCbPrefs
            = new ArrayList<CheckBoxPreference>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.crdroid_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mContext = getActivity();

	int collapseBehaviour = Settings.System.getInt(getContentResolver(),
                    Settings.System.STATUS_BAR_COLLAPSE_ON_DISMISS,
                    Settings.System.STATUS_BAR_COLLAPSE_IF_NO_CLEARABLE);
        mCollapseOnDismiss = (ListPreference) prefSet.findPreference(UI_COLLAPSE_BEHAVIOUR);
        mCollapseOnDismiss.setValue(String.valueOf(collapseBehaviour));
        mCollapseOnDismiss.setOnPreferenceChangeListener(this);
        updateCollapseBehaviourSummary(collapseBehaviour); 

        int CurrentBehavior = Settings.System.getInt(getContentResolver(), Settings.System.NOTIFICATIONS_BEHAVIOUR, 0);
        mNotificationsBehavior = (ListPreference) findPreference(UI_NOTIFICATION_BEHAVIOUR);
        mNotificationsBehavior.setValue(String.valueOf(CurrentBehavior));
        mNotificationsBehavior.setSummary(mNotificationsBehavior.getEntry());
        mNotificationsBehavior.setOnPreferenceChangeListener(this);
	
	mNoNotificationsPulldown = (ListPreference) prefSet.findPreference(NO_NOTIFICATIONS_PULLDOWN);
        int noNotificationsPulldownValue = Settings.System.getInt(getContentResolver(), Settings.System.QS_NO_NOTIFICATION_PULLDOWN, 0);
        mNoNotificationsPulldown.setValue(String.valueOf(noNotificationsPulldownValue));
        updateNoNotificationsPulldownSummary(noNotificationsPulldownValue);
	mNoNotificationsPulldown.setOnPreferenceChangeListener(this);
        
	mWakeUpWhenPluggedOrUnplugged = (CheckBoxPreference) findPreference(KEY_WAKEUP_WHEN_PLUGGED_UNPLUGGED);
        mWakeUpWhenPluggedOrUnplugged.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                        Settings.System.WAKEUP_WHEN_PLUGGED_UNPLUGGED, 1) == 1);

	mStatusBarQuickPeek = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_QUICK_PEEK);
        mStatusBarQuickPeek.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.STATUSBAR_PEEK, 0) == 1)); 
	
	mStatusBarTrafficEnable = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_TRAFFIC_ENABLE);
        mStatusBarTrafficEnable.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.STATUS_BAR_TRAFFIC_ENABLE, 0) == 1));

        mStatusBarTrafficHide = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_TRAFFIC_HIDE);
        mStatusBarTrafficHide.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.STATUS_BAR_TRAFFIC_HIDE, 1) == 1));  

	mStatusBarTrafficSummary = (ListPreference) findPreference(STATUS_BAR_TRAFFIC_SUMMARY);
        mStatusBarTrafficSummary.setOnPreferenceChangeListener(this);
        mStatusBarTrafficSummary.setValue((Settings.System.getInt(getActivity().getContentResolver(),
                        Settings.System.STATUS_BAR_TRAFFIC_SUMMARY, 3000)) + "");

	// Low battery warning
        mLowBatteryWarning = (ListPreference) findPreference(KEY_LOW_BATTERY_WARNING_POLICY);
        int lowBatteryWarning = Settings.System.getInt(getActivity().getContentResolver(),
                                    Settings.System.POWER_UI_LOW_BATTERY_WARNING_POLICY, 0);
        mLowBatteryWarning.setValue(String.valueOf(lowBatteryWarning));
        mLowBatteryWarning.setSummary(mLowBatteryWarning.getEntry());
        mLowBatteryWarning.setOnPreferenceChangeListener(this);

	int statusScreenOnNotificationLed = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_ON_NOTIFICATION_LED, 1);
        mScreenOnNotificationLed = (CheckBoxPreference) findPreference(KEY_SCREEN_ON_NOTIFICATION_LED);
        mScreenOnNotificationLed.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.SCREEN_ON_NOTIFICATION_LED, 0) == 1);     

	mCustomLabel = findPreference(PREF_CUSTOM_CARRIER_LABEL);
        updateCustomLabelTextSummary();

	mShowWifiName = (CheckBoxPreference) findPreference(PREF_NOTIFICATION_SHOW_WIFI_SSID);
        mShowWifiName.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.NOTIFICATION_SHOW_WIFI_SSID, 0) == 1);

	mFlipQsTiles = (CheckBoxPreference) findPreference(PREF_FLIP_QS_TILES);
        mFlipQsTiles.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.QUICK_SETTINGS_TILES_FLIP, 1) == 1);  

	mKillAppLongpressBack = findAndInitCheckboxPref(KILL_APP_LONGPRESS_BACK);

	mRamBar = findPreference(KEY_RECENTS_RAM_BAR);
        updateRamBar();

	mUseAltResolver = (CheckBoxPreference) findPreference(PREF_USE_ALT_RESOLVER);
        mUseAltResolver.setChecked(Settings.System.getInt(
                getActivity().getContentResolver(),
                Settings.System.ACTIVITY_RESOLVER_USE_ALT, 0) == 1);

	// Media scan behavior
	int MSOB = Settings.System.getInt(getActivity().getContentResolver(),
			Settings.System.MEDIA_SCANNER_ON_BOOT, 0);
	mMSOB = (ListPreference) findPreference(MEDIA_SCANNER_ON_BOOT);	
	mMSOB.setValue(String.valueOf(MSOB));
	mMSOB.setSummary(mMSOB.getEntry());
	mMSOB.setOnPreferenceChangeListener(this);

	// Less notification sound
	mAnnoyingNotifications = (ListPreference) findPreference(PREF_LESS_NOTIFICATION_SOUNDS);
        mAnnoyingNotifications.setOnPreferenceChangeListener(this);
        int notificationThreshold = Settings.System.getInt(getContentResolver(),
                Settings.System.MUTE_ANNOYING_NOTIFICATIONS_THRESHOLD, 0);
        mAnnoyingNotifications.setValue(Integer.toString(notificationThreshold));	
        
	mCrdroidCategory = (PreferenceCategory) prefSet.findPreference(CRDROID_CATEGORY);

    }

    @Override
    public void onResume() {
        super.onResume();
 
        updateKillAppLongpressBackOptions();
	updateRamBar(); 
    }

    private void writeKillAppLongpressBackOptions() {
        Settings.Secure.putInt(getActivity().getContentResolver(),
                Settings.Secure.KILL_APP_LONGPRESS_BACK,
                mKillAppLongpressBack.isChecked() ? 1 : 0);
    }

    private void updateKillAppLongpressBackOptions() {
        mKillAppLongpressBack.setChecked(Settings.Secure.getInt(
            getActivity().getContentResolver(), Settings.Secure.KILL_APP_LONGPRESS_BACK, 0) != 0);
    }

    private void updateCustomLabelTextSummary() {
        mCustomLabelText = Settings.System.getString(getActivity().getContentResolver(),
                Settings.System.CUSTOM_CARRIER_LABEL);
        if (mCustomLabelText == null || mCustomLabelText.length() == 0) {
            mCustomLabel.setSummary(R.string.custom_carrier_label_notset);
        } else {
            mCustomLabel.setSummary(mCustomLabelText);
        }
    }

    private CheckBoxPreference findAndInitCheckboxPref(String key) {
        CheckBoxPreference pref = (CheckBoxPreference) findPreference(key);
        if (pref == null) {
            throw new IllegalArgumentException("Cannot find preference with key = " + key);
        }
        mAllPrefs.add(pref);
        mResetCbPrefs.add(pref);
        return pref;
    }   

    @Override
    public void onPause() {
        super.onPause();
	updateRamBar();
    }

    private void updateRamBar() {
        int ramBarMode = Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.RECENTS_RAM_BAR_MODE, 0);
        if (ramBarMode != 0)
            mRamBar.setSummary(getResources().getString(R.string.ram_bar_color_enabled));
        else
            mRamBar.setSummary(getResources().getString(R.string.ram_bar_color_disabled));
    }

    private void updateNoNotificationsPulldownSummary(int value) {

        if (value == 0) {
            /* No Notifications Pulldown deactivated */
            mNoNotificationsPulldown.setSummary(getResources().getString(R.string.no_notifications_pulldown_off));
        } else {
            mNoNotificationsPulldown.setSummary(getResources().getString(value == 1
                    ? R.string.no_notifications_pulldown_summary_nonperm
                    : R.string.no_notifications_pulldown_summary_all));
        }
    }

    private void updateCollapseBehaviourSummary(int setting) {
        String[] summaries = getResources().getStringArray(
                R.array.notification_drawer_collapse_on_dismiss_summaries);
        mCollapseOnDismiss.setSummary(summaries[setting]);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	boolean value;	

	if (preference == mStatusBarQuickPeek) {
            value = mStatusBarQuickPeek.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUSBAR_PEEK, value ? 1 : 0);
            return true;	
	} else if (preference == mKillAppLongpressBack) {
            writeKillAppLongpressBackOptions();
	} else if (preference == mCustomLabel) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle(R.string.custom_carrier_label_title);
            alert.setMessage(R.string.custom_carrier_label_explain);

            // Set an EditText view to get user input
            final EditText input = new EditText(getActivity());
            input.setText(mCustomLabelText != null ? mCustomLabelText : "");
            alert.setView(input);
            alert.setPositiveButton(getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = ((Spannable) input.getText()).toString();
                    Settings.System.putString(getActivity().getContentResolver(),
                            Settings.System.CUSTOM_CARRIER_LABEL, value);
                    updateCustomLabelTextSummary();

              Intent i = new Intent();
                    i.setAction("com.android.settings.LABEL_CHANGED");
                    getActivity().sendBroadcast(i); 
                }
            });
            alert.setNegativeButton(getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
	} else if (preference == mWakeUpWhenPluggedOrUnplugged) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.WAKEUP_WHEN_PLUGGED_UNPLUGGED,
                    mWakeUpWhenPluggedOrUnplugged.isChecked() ? 1 : 0);
            return true;
	} else if (preference == mUseAltResolver) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.ACTIVITY_RESOLVER_USE_ALT,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
	} else if (preference == mShowWifiName) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NOTIFICATION_SHOW_WIFI_SSID,
                    mShowWifiName.isChecked() ? 1 : 0);
            return true;
	} else if (preference == mFlipQsTiles) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.QUICK_SETTINGS_TILES_FLIP,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
	} else if (preference == mScreenOnNotificationLed) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.SCREEN_ON_NOTIFICATION_LED,
                    mScreenOnNotificationLed.isChecked() ? 1 : 0);
	    return true;
	} else if (preference == mStatusBarTrafficEnable) {
            value = mStatusBarTrafficEnable.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_TRAFFIC_ENABLE, value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarTrafficHide) {
            value = mStatusBarTrafficHide.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_TRAFFIC_HIDE, value ? 1 : 0);
            return true;       
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
	if (preference == mCollapseOnDismiss) {
            int value = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_COLLAPSE_ON_DISMISS, value);
            updateCollapseBehaviourSummary(value);
            return true;         
	} else if (preference == mNotificationsBehavior) {
            String val = (String) newValue;
                    Settings.System.putInt(getContentResolver(), Settings.System.NOTIFICATIONS_BEHAVIOUR,
            Integer.valueOf(val));
            int index = mNotificationsBehavior.findIndexOfValue(val);
            mNotificationsBehavior.setSummary(mNotificationsBehavior.getEntries()[index]);
            return true;
	} else if (preference == mNoNotificationsPulldown) {
            int noNotificationsPulldownValue = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.QS_NO_NOTIFICATION_PULLDOWN,
                    noNotificationsPulldownValue);
            updateNoNotificationsPulldownSummary(noNotificationsPulldownValue);
            return true;
	} else if (preference == mLowBatteryWarning) {
            int lowBatteryWarning = Integer.valueOf((String) newValue);
            int index = mLowBatteryWarning.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_UI_LOW_BATTERY_WARNING_POLICY,
                    lowBatteryWarning);
            mLowBatteryWarning.setSummary(mLowBatteryWarning.getEntries()[index]);
            return true;
	} else if (preference == mStatusBarTrafficSummary) {
            int val = Integer.valueOf((String) newValue);
            int index = mStatusBarTrafficSummary.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(), 
		    Settings.System.STATUS_BAR_TRAFFIC_SUMMARY, val);
            mStatusBarTrafficSummary.setSummary(mStatusBarTrafficSummary.getEntries()[index]);
            return true;
	} else if (preference == mMSOB) {
            int MSOB = Integer.valueOf((String) newValue);
	    int index = mMSOB.findIndexOfValue((String) newValue);
	    Settings.System.putInt(getActivity().getContentResolver(),
		    Settings.System.MEDIA_SCANNER_ON_BOOT, MSOB);
	    mMSOB.setSummary(mMSOB.getEntries()[index]);	
	    return true;
	} else if (preference == mAnnoyingNotifications) {
            final int val = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.MUTE_ANNOYING_NOTIFICATIONS_THRESHOLD, val);      
	}  
        return false;
    }
}
