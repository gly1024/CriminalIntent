package com.jack.criminalintent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jack.criminalintent.CrimeActivity;
import com.jack.criminalintent.R;
import com.jack.criminalintent.data.Crime;
import com.jack.criminalintent.data.CrimeLab;
import com.jack.criminalintent.utils.PictureUtils;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @author tesla
 */
public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    
    private static final String ARG_CRIME_ID = "crime_id";

    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private static final int REQUEST_CONTACT = 1;

    private static final int REQUEST_PHOTO = 2;

    private Crime mCrime;

    private File mPhotoFile;

    private EditText mTitleEditView;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;
    
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Method 1
        /*UUID crimeId = (UUID) getActivity().getIntent()
            .getSerializableExtra(CrimeActivity.getExtraCrimeIdKey());*/

        // Method 2
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        Context context = getActivity();
        mCrime = CrimeLab.getInstance(context).getCrime(crimeId);
        mPhotoFile = CrimeLab.getInstance(context).getPhotoFile(mCrime);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
         Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (intent == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATE:
                Date date = (Date) intent.getSerializableExtra(DatePickerFragment.getExtraDate());
                mCrime.setDate(date);
                mDateButton.setText(DateFormat.getInstance().format(mCrime.getDate()));
                return;
            case REQUEST_CONTACT:
                handleContactIntent(intent);
                return;
            case REQUEST_PHOTO:
                Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.jack.criminalintent.fileprovider", mPhotoFile);
                getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updatePhotoView();
                return;
            default:
                Log.i(TAG, "onActivityResult default");
        }

    }

    private void handleContactIntent(Intent intent) {
        Uri contactUri = intent.getData();
        String[] queryFields = {ContactsContract.Contacts.DISPLAY_NAME};

        try (Cursor cursor = getActivity().getContentResolver()
            .query(contactUri, queryFields, null, null, null)) {
            if (cursor.getCount() == 0) {
                return;
            }
            cursor.moveToFirst();
            String suspect = cursor.getString(0);
            mCrime.setSuspect(suspect);
            mSuspectButton.setText(suspect);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Uri uri = FileProvider.getUriForFile(getActivity(),
            "com.jack.criminalintent.fileprovider", mPhotoFile);
        getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }

    private void initView(View view) {
        mTitleEditView = view.findViewById(R.id.crime_title);
        mTitleEditView.setText(mCrime.getTitle());

        mDateButton = view.findViewById(R.id.crime_date);
        mDateButton.setText(DateFormat.getInstance().format(mCrime.getDate()));
        mDateButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getFragmentManager();
            DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
            dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
            dialog.show(fragmentManager, DIALOG_DATE);
        });

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

        mReportButton = view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
            intent = Intent.createChooser(intent, getString(R.string.send_report));
            startActivity(intent);
        });


        final Intent pickContact =
            new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton = view.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(v -> {
            startActivityForResult(pickContact, REQUEST_CONTACT);
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoView = view.findViewById(R.id.crime_photo);
        mPhotoButton = view.findViewById(R.id.crime_camera);
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto =
            (mPhotoFile != null) && (captureIntent.resolveActivity(packageManager) != null);
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(v -> {
            Uri uri =
                FileProvider.getUriForFile(getActivity(), "com.jack.criminalintent.fileprovider", mPhotoFile);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            List<ResolveInfo> cameraActivities =
                getActivity().getPackageManager().queryIntentActivities(captureIntent,
                    PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo activity : cameraActivities) {
                getActivity().grantUriPermission(activity.activityInfo.packageName, uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            startActivityForResult(captureIntent, REQUEST_PHOTO);
        });
        updatePhotoView();
    }

    private String getCrimeReport() {
        String solvedStr = null;
        if (mCrime.isSolved()) {
            solvedStr = getString(R.string.crime_report_solved);
        } else {
            solvedStr = getString(R.string.crime_report_unsolved);
        }

        final String dateFormat = "EEE, MMM dd";
        String dateStr = android.text.format.DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        return getString(R.string.crime_report, mCrime.getTitle(), dateStr, solvedStr, suspect);
    }

    private void updatePhotoView() {
        // todo 图片为啥显示不出来
        Log.i(TAG, "updatePhotoView");
        if ((mPhotoFile == null) || !mPhotoFile.exists()) {
            Log.e(TAG, "photoFile is nut exist!");
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            Log.i(TAG, "bitmap is " + bitmap.toString());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}