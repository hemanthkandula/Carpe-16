package com.myapplication.siva.Carpedium.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SqliteDB extends SQLiteOpenHelper {

	public SqliteDB(Context applicationcontext) {
		super(applicationcontext, "usersqlite.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE users ( userId INTEGER PRIMARY KEY, userName TEXT,udpateStatus TEXT,EventNo TEXT NOT NULL DEFAULT '0',GroupName TEXT NOT NULL DEFAULT 'G',Position TEXT NOT NULL DEFAULT 'P',Rank INT DEFAULT 0,RegiNo INT)";
		database.execSQL(query);
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS users";
		database.execSQL(query);
		onCreate(database);
	}

	public void insertUser(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("RegiNo", queryValues.get("RegiNo"));
        values.put("EventNo",queryValues.get("EventNo"));
        if(queryValues.containsKey("GroupName"))
        { values.put("GroupName",queryValues.get("GroupName"));}
		values.put("udpateStatus", "no");

		database.insert("users", null, values);
		database.close();
	}




	public ArrayList<HashMap<String, String>> getAllUsers(String EventNo) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM users WHERE EventNo="+"'"+ EventNo +"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
					map.put("updateStatus", cursor.getString(7));
					if (cursor.getString(1) == "null") {
						map.put("RegiNo", cursor.getString(7));
					} else {
						{
							map.put("RegiNo", cursor.getString(1));
						}
					}



				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		return wordList;
	}



	public ArrayList<HashMap<String, String>> getAllGroupUsers(String EventNo,String GroupName) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM users WHERE EventNo="+"'"+ EventNo +"' and GroupName =  "+"'"+ GroupName +  "'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("updateStatus", cursor.getString(7));
				if(cursor.getString(1)== null)
				{map.put("RegiNo", cursor.getString(7));}
				else {                {map.put("RegiNo", cursor.getString(1));}
				}



				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		return wordList;
	}


	public String composeJSONfromSQLite(){
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM users where udpateStatus = '"+"no"+"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("userId", cursor.getString(0));

                map.put("RegiNo", cursor.getString(7));


				map.put("EventNo", cursor.getString(3));
                map.put("GroupName", cursor.getString(4));
                map.put("Position",cursor.getString(5));
                map.put("Rank", cursor.getString(6));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		Gson gson = new GsonBuilder().create();

		return gson.toJson(wordList);
	}
    public ArrayList<HashMap<String, String>> getAllGroups(String EventNo) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT DISTINCT  GroupName FROM users WHERE EventNo="+"'"+ EventNo +"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                //map.put("userId", cursor.getString(0));
                map.put("GroupName", cursor.getString(0));



                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
		System.out.println(wordList);
        return wordList;
    }




    public String getSyncStatus(){
		String msg = null;
		if(this.dbSyncCount() == 0){
			msg = "in Sync!";
		}else{
			msg = "Sync needed\n";
		}
		return msg;
	}


	public int dbSyncCount(){
		int count = 0;
		String selectQuery = "SELECT  * FROM users where udpateStatus = '"+"no"+"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		count = cursor.getCount();
		database.close();
		return count;
	}


	public void updateSyncStatus(String id, String status,String userName){
		SQLiteDatabase database = this.getWritableDatabase();
		String updateQuery = "Update users set userName = '"+ userName +"' , udpateStatus = '"+ status +"' where userId="+"'"+ id +"'";
		Log.d("query",updateQuery);
		database.execSQL(updateQuery);
		database.close();
	}

	public void SetRank(String RegiNo,String Rank) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update users set Rank = '"+ Rank +"' where userName="+"'"+ RegiNo +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();

	}

    public void SetGroupRank(String GroupName,String Rank) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update users set Rank = '"+ Rank +"' where GroupName="+"'"+ GroupName +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }




}
