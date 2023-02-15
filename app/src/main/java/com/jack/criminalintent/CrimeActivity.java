package com.jack.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.jack.criminalintent.fragment.CrimeFragment;

/**
 * @author tesla
 */
public class CrimeActivity extends SingelFragmentActivity {
    private static final String TAG = "CrimeActivity";

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}