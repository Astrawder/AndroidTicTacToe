package com.example.tictactoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.app.Fragment;

public class SettingsFragment extends PreferenceFragment
implements OnSharedPreferenceChangeListener {

    private SharedPreferences preferences;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SecondaryActivityFragment secondaryActivityFragment =
                (SecondaryActivityFragment) getFragmentManager()
                        .findFragmentById(R.id.Secondary_Fragment);
        if(secondaryActivityFragment != null){
            secondaryActivityFragment.onResume();
        }
    }
}
