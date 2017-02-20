package com.myapplication.siva.Carpedium;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.myapplication.siva.Carpedium.Databases.ResulltsDB;
import com.myapplication.siva.Carpedium.Databases.SqliteDB;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupMembersresults extends AppCompatActivity {
    ArrayList<HashMap<String, String>> userList;
    SharedPreferences prefs;
    String password;
    public static final String UserPref = "Userpref";
    public static final String Regikey = "RegiKey";
    public static final String Passkey = "PassKey";
String EventNo;
    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton floatingActionButton1,floatingActionButton2;

    HashMap<String, String> queryValues = new HashMap<String, String>();
    ListAdapter adapter;
    String m_Text;
    //SqliteDB sqliteDB;
    ResulltsDB resulltsDB;
    String GroupName;
    String regino;
    String results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        final ListView Memlist = (ListView)findViewById(R.id.memlist);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.design_floating_action_menu_item1);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.design_floating_action_menu_item2);

       // sqliteDB = new SqliteDB(this);
        prefs = getApplicationContext().getSharedPreferences(UserPref, Context.MODE_PRIVATE);
        resulltsDB= new ResulltsDB(this);

        EventNo = getIntent().getStringExtra("EventNo");
        GroupName = getIntent().getStringExtra("GroupName");
        regino = getIntent().getStringExtra("regino");
        results = getIntent().getStringExtra("results");

        getSupportActionBar().setTitle(GroupName);
        Log.d("EvenNo",EventNo);
        Log.d("Groupname",GroupName);
        if(regino!= null)
        {Log.d("Regino",regino);}
        if (regino != null) {

            queryValues.put("RegiNo", regino);
            queryValues.put("EventNo", EventNo);
            queryValues.put("GroupName", GroupName);


            resulltsDB.insertUser(queryValues);


        }




        // regno =getPreference(this,Regikey);
        // password =getPreference(this,Passkey);
        if (prefs.contains(Regikey)&&prefs.contains(Passkey))
        {
            password = prefs.getString(Passkey,null);
        }

        if(results ==null)
        { //Log.d("use",results);
        userList = resulltsDB.getallGroupUsers(GroupName);
        }
        else
        {Log.d("RES",results);
            userList = resulltsDB.getallGroupUsers(GroupName);
        }


        if (userList.size() != 0) {
             adapter= new SimpleAdapter(getApplicationContext(), userList, R.layout.swipeviewrowitem, new String[]{"Position", "RegiNo"}, new int[]{R.id.use, R.id.mainText});
            Memlist.setAdapter(adapter);
            Toast.makeText(getApplicationContext(), resulltsDB.getSyncStatus(), Toast.LENGTH_LONG).show();

        }




        Memlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


           ////here app cant  create dialogs!!

                final String[] position1 = {""};


                AlertDialog.Builder builder2 = new AlertDialog.Builder(GroupMembersresults.this);
                builder2.setTitle("Choose Position");


                final EditText input = new EditText(GroupMembersresults.this);
                input.setHint("Eg. BEST DIRECTOR");
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextColor(Color.BLACK);
                builder2.setView(input);
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position1[0] = input.getText().toString();

//dismissing the dialog when the user makes a selection.

                        final AlertDialog.Builder builder3 = new AlertDialog.Builder(GroupMembersresults.this);
                        builder3.setTitle("ENTER PASSWORD");

                        final EditText input1 = new EditText(GroupMembersresults.this);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        input1.setTextColor(Color.BLACK);
                        builder3.setView(input1);
                        final TextView text = new TextView(GroupMembersresults.this);
                        text.setText("");
                        builder3.setView(input1);
                        builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(password.equals(input1.getText().toString()))
                                {
                                    text.setText("Password Ok");
                                    text.setTextColor(Color.GREEN);

                    /*XXXXXXXXXXXXXXXXXX ADD CODE HERE XXXXXXXXXXXXXXXXXXXXXXXXXX*/


//                                    HashMap<String, String> q = new HashMap<String, String>();
                                    HashMap<String, String> User = userList.get(position);
                                    System.out.println( User.get("RegiNo"));

                                    User.put("EventNo",EventNo);
                                    User.put("GroupName",GroupName);
                                    User.put("userName",userList.get(position).get("RegiNo"));
                                    User.put("Position",position1[0]);
                                    System.out.println(User+"pos");
                                    resulltsDB.updateposition(User);
                                    System.out.println(User+"pos1s");

                                    userList =resulltsDB.getAllGroupstuds(EventNo);
                                    adapter = new SimpleAdapter(GroupMembersresults.this, userList, R.layout.swipeviewrowitem, new String[]{"Rank", "GroupName"}, new int[]{R.id.use, R.id.mainText});

                                    Memlist.setAdapter(adapter);


                                    dialog.dismiss();
                                }
                                else
                                {
                                    text.setText("Password Wrong");
                                    text.setTextColor(Color.RED);
                                }



                            }
                        });
                        builder3.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                        dialog.dismiss();

                        AlertDialog alertdialog3 = builder3.create();
                        alertdialog3.show();
                    }

                });


                AlertDialog alertdialog2 = builder2.create();
                alertdialog2.show();
            }
        });

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB Action goes here
               // Intent objIntent = new Intent(GroupMembers.class, SimpleScannerActivity.class);
                Intent objIntent =new Intent(getApplicationContext() ,SimpleScannerActivity.class);
                objIntent.putExtra("EventNo", EventNo);
                objIntent.putExtra("GroupName", GroupName);


                startActivity(objIntent);
                finish();
            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB Action goes here
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupMembersresults.this);
                builder.setTitle("Enter Registration Number");

                // Set up the input
                final EditText input = new EditText(GroupMembersresults.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if (m_Text != null) {

                            queryValues.put("RegiNo", m_Text);
                            queryValues.put("EventNo", EventNo);
                            queryValues.put("GroupName", GroupName);


                            resulltsDB.insertUser(queryValues);
                            Log.d("AlretQuery","queryValues");
                            ListAdapter adapter = new SimpleAdapter(getApplicationContext(), userList, R.layout.swipeviewrowitem, new String[]{"Position", "RegiNo"}, new int[]{R.id.use, R.id.mainText});
                            Memlist.setAdapter(adapter);
                            System.out.println(queryValues);
                            dialog.cancel();
                        }

                        userList =resulltsDB.getAllGroupstuds(EventNo);
                        adapter = new SimpleAdapter(GroupMembersresults.this, userList, R.layout.swipeviewrowitem, new String[]{"Rank", "GroupName"}, new int[]{R.id.use, R.id.mainText});

                        Memlist.setAdapter(adapter);

//                        Intent objIntent = new Intent(GroupMembers.this, GroupMembers.class);
////
//                        objIntent.putExtra("EventNo", EventNo);


//                        startActivity(objIntent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ter, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.done)
        {
            Intent objIntent =new Intent(this ,GroupStudentsadd.class);
            objIntent.putExtra("EventNo", EventNo);
            objIntent.putExtra("GroupName", GroupName);
            startActivity(objIntent);
            finish();
            return true;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }



}
