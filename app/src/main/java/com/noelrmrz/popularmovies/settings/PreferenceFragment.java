package com.noelrmrz.popularmovies.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.noelrmrz.popularmovies.R;

public class PreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
