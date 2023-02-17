package com.jack.criminalintent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jack.criminalintent.CrimeActivity;
import com.jack.criminalintent.CrimeListActivity;
import com.jack.criminalintent.CrimePagerActivity;
import com.jack.criminalintent.R;
import com.jack.criminalintent.data.Crime;
import com.jack.criminalintent.data.CrimeLab;

import java.text.DateFormat;
import java.util.List;

/**
 * @author tesla
 */
public class CrimeListFragment extends Fragment {
    private static final String TAG = "CrimeListFragment";

    private RecyclerView mCrimeRecyclerView;

    private CrimeAdapter mCirmeAdapter;

    private int mPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mCirmeAdapter == null) {
            mCirmeAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mCirmeAdapter);
        } else {
            //刷新全部item
            mCirmeAdapter.notifyDataSetChanged();
            // 如何局部刷新单个item
//            mCirmeAdapter.notifyItemChanged(mPosition);
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        private Crime mCrime;

        private int mTargetPosition;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }

        public void bindDate(Crime crime, int position) {
            mCrime = crime;
            mTargetPosition = position;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(DateFormat.getInstance().format(mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            // 点击时记录当前position
            mPosition = mTargetPosition;
            Log.i(TAG, "position is :" + mPosition);
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindDate(crime, position);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
