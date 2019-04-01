package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.fragment.StepDetailsFragment;
import com.example.android.bakingapp.object.Step;

import java.util.ArrayList;

import static com.example.android.bakingapp.fragment.StepDetailsFragment.STEP_LIST;
import static com.example.android.bakingapp.fragment.StepDetailsFragment.STEP_POSITION;

public class StepDetailsActivity extends AppCompatActivity implements StepDetailsFragment.OnButtonClickListener{

    private ArrayList<Step> mStepList;
    private int mStepPosition;
    private StepDetailsFragment mStepDetailsFragment;
    private FragmentManager mFragmentManager;

    private static final String DETAILS_FRAGMENT = "details_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        mFragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(STEP_LIST) && intent.hasExtra(STEP_POSITION)) {
            mStepList = intent.getParcelableArrayListExtra(STEP_LIST);
            mStepPosition = intent.getIntExtra(STEP_POSITION, 0);
        }

        setTitle(mStepList.get(mStepPosition).getShortDesc());

        if (savedInstanceState == null) {
            mStepDetailsFragment = new StepDetailsFragment();

            mStepDetailsFragment.setStepPosition(mStepPosition);
            mStepDetailsFragment.setStepList(mStepList);

            mFragmentManager.beginTransaction()
                    .add(R.id.step_details_container, mStepDetailsFragment)
                    .commit();
        } else {
            mStepDetailsFragment = (StepDetailsFragment) mFragmentManager.getFragment(savedInstanceState, DETAILS_FRAGMENT);

            mFragmentManager.beginTransaction()
                    .replace(R.id.step_details_container, mStepDetailsFragment)
                    .commit();
        }
    }

    @Override
    public void onNextPressed(int position) {
        replaceDetailsFragment(position);
    }

    @Override
    public void onPreviousPressed(int position) {
        replaceDetailsFragment(position);
    }

    private void replaceDetailsFragment(int position) {
        mStepDetailsFragment = new StepDetailsFragment();
        mStepDetailsFragment.setStepList(mStepList);
        mStepDetailsFragment.setStepPosition(position);

        setTitle(mStepList.get(position).getShortDesc());

        mFragmentManager.beginTransaction()
                .replace(R.id.step_details_container, mStepDetailsFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentManager.putFragment(outState, DETAILS_FRAGMENT, mStepDetailsFragment);
    }
}
