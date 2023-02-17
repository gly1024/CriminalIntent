package com.jack.criminalintent;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.jack.criminalintent.fragment.CrimeFragment;

import java.util.UUID;

/**
 * @author tesla
 */
public class CrimeActivity extends SingelFragmentActivity {
    private static final String TAG = "CrimeActivity";

    private static final String EXTRA_CRIME_ID = "com.jack.criminalintent.crime_id";

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
}