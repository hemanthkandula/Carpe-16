package com.myapplication.siva.Carpedium;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

public class studentsaddActivity extends AppCompatActivity {

	FragmentManager mFragmentManager;
	FragmentTransaction mFragmentTransaction;

	// Tab titles
	private String[] tabs = { "ADD", "ALL" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_std);

		mFragmentManager = getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();


	}
	protected void onActivityCreated(Bundle bundle){}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_terminal, menu);
		return true;
	}
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.refresh) {
			Intent i = new Intent(this,studentsaddActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
public void reload(){

}

}