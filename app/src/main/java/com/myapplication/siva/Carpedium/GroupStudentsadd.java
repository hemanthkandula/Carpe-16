package com.myapplication.siva.Carpedium;

/**
 * Created by Siva Subramanian L on 04-10-2016.
 */

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.app.ActionBar;

public class GroupStudentsadd extends AppCompatActivity {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    // Tab titles
    private String[] tabs = {"ADD", "ALL"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabsFragment()).commit();

    }

    protected void onActivityCreated(Bundle bundle) {
    }

}