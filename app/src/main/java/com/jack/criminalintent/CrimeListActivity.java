package com.jack.criminalintent;

import androidx.fragment.app.Fragment;

import com.jack.criminalintent.fragment.CrimeListFragment;

/**
 * @author tesla
 */
public class CrimeListActivity extends SingelFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
