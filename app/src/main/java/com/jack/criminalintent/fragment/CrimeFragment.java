package com.jack.criminalintent.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jack.criminalintent.CrimeActivity;
import com.jack.criminalintent.R;
import com.jack.criminalintent.data.Crime;
import com.jack.criminalintent.data.CrimeLab;

import java.text.DateFormat;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @author tesla
 */
public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";

    private Crime mCrime;

    private EditText mTitleEditView;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getActivity().getIntent()
            .getSerializableExtra(CrimeActivity.getExtraCrimeIdKey());
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
         Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTitleEditView = view.findViewById(R.id.crime_title);
        mTitleEditView.setText(mCrime.getTitle());
        mDateButton = view.findViewById(R.id.crime_date);
        mDateButton.setText(DateFormat.getInstance().format(mCrime.getDate()));
        mDateButton.setEnabled(false);
        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->
            mSolvedCheckBox.setChecked(isChecked));
        mTitleEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}