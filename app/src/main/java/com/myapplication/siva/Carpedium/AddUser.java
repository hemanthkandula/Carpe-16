package com.myapplication.siva.Carpedium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myapplication.siva.Carpedium.Databases.SqliteDB;

import java.util.HashMap;

public class AddUser extends Activity {
	EditText userName;
	SqliteDB controller = new SqliteDB(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_user);
		userName = (EditText) findViewById(R.id.userName);
	}
	

	public void addNewUser(View view) {
		HashMap<String, String> queryValues = new HashMap<String, String>();
		queryValues.put("userName", userName.getText().toString());
		if (userName.getText().toString() != null
				&& userName.getText().toString().trim().length() != 0) {
			controller.insertUser(queryValues);
			this.callHomeActivity(view);
		} else {
			Toast.makeText(getApplicationContext(), "Please enter User name",
					Toast.LENGTH_LONG).show();
		}
	}
	public void callHomeActivity(View view) {
		Intent objIntent = new Intent(getApplicationContext(),
				studentsaddActivity.class);
		startActivity(objIntent);
	}


	public void cancelAddUser(View view) {
		this.callHomeActivity(view);
	}
}
