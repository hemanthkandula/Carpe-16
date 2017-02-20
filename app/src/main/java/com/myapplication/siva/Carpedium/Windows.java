package com.myapplication.siva.Carpedium;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myapplication.siva.Carpedium.Databases.SqliteDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Windows extends Fragment   implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    SqliteDB controller;
    ProgressDialog prgDialog;
    String EventNo;
    ListView myList;
//   FloatingActionMenu materialDesignFAM;
//    com.github.clans.fab.FloatingActionButton floatingActionButton1,floatingActionButton2,floatingActionButton3;

    android.support.design.widget.FloatingActionButton fab;
    String m_Text,m_Text2;
    HashMap<String, String> queryValues = new HashMap<String, String>();
    HashMap<String, String> queryValues2 = new HashMap<String, String>();


    Context thiscontext;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = container.getContext();

        View windows = inflater.inflate(R.layout.windows_frag, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) windows.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener( this);
//        swipeRefreshLayout.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                       // swipeRefreshLayout.setRefreshing(true);
//
//                                        syncSQLiteMySQLDB();
//
//                                    }
//                                }
//        );
        String regino = null;
        fab = (android.support.design.widget.FloatingActionButton) windows.findViewById(R.id.fab);
//        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) windows.findViewById(R.id.material_menu_item1);
//        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) windows.findViewById(R.id.materialmenu_item2);
        //floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) windows.findViewById(R.id.material_item3);

        controller = new SqliteDB(getActivity());
        regino = getActivity().getIntent().getStringExtra("regino");

        EventNo = getActivity().getIntent().getStringExtra("EventNo");
        System.out.println(EventNo);
        if (regino != null) {

            queryValues.put("RegiNo", regino);
            queryValues.put("EventNo", EventNo);

            controller.insertUser(queryValues);


        }
        myList = (ListView) windows.findViewById(R.id.studlist);

        final ArrayList<HashMap<String, String>> userList = controller.getAllGroups(EventNo);



        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                switch (position) {

                    default:
                        System.out.println(userList.get(position).get("GroupName"));
                        //Log.d("Gflag",clusterDB.AmGroup(no));
                        Intent Cl = new Intent(getActivity(), GroupMembers.class);
                        Cl.putExtra("EventNo", EventNo);
                        Cl.putExtra("GroupName", userList.get(position).get("GroupName"));

                        startActivity(Cl);
                        break;


                    // Nothing do!
                }
            } });




        if (userList.size() != 0) {
            ListAdapter adapter = new SimpleAdapter(thiscontext, userList, R.layout.swipeviewrowitem, new String[]{"userId", "GroupName"}, new int[]{R.id.use, R.id.mainText});
            myList.setAdapter(adapter);
            Toast.makeText(thiscontext, controller.getSyncStatus(), Toast.LENGTH_LONG).show();

        }


        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Synching SQLite Data with Remote MySQL DB. Please wait...");
        prgDialog.setCancelable(false);
       /* floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB Action goes here
                Intent objIntent = new Intent(getActivity(), SimpleScannerActivity.class);
                objIntent.putExtra("EventNo", EventNo);
                startActivity(objIntent);
                getActivity().finish();
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncSQLiteMySQLDB();
            }
        });*/


//        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                syncSQLiteMySQLDB();
//
//
//            }});


         fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB Action goes here
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Team");
                LinearLayout layout = new LinearLayout(thiscontext);
                layout.setOrientation(LinearLayout.VERTICAL);
                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("Enter team name");
                layout.addView(input);


                final EditText input2 = new EditText(thiscontext);
                input2.setHint("Enter Register Number of Team Head");
                input2.setInputType(InputType.TYPE_CLASS_NUMBER);
                input2.setRawInputType(Configuration.KEYBOARD_12KEY);
                layout.addView(input2);



                builder.setView(layout);
                
                

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        m_Text2 = input2.getText().toString();
                        
                        if (m_Text != null && m_Text2 != null){

                            Log.d("Alert",m_Text);
                            Log.d("Alert",m_Text2);


                        queryValues2.put("RegiNo", m_Text2);
                        queryValues2.put("EventNo", EventNo);
                            queryValues2.put("GroupName",m_Text);
                            System.out.println("GIN"+queryValues2);
                            controller.insertUser(queryValues2);

                            dialog.cancel();
                        }
                        if(input.getText().toString() != null && input2.getText().toString() != null){





                        Intent objIntent = new Intent(getActivity(),GroupMembers.class);
//
                        objIntent.putExtra("EventNo", EventNo);
                            objIntent.putExtra("GroupName", m_Text);


                            startActivity(objIntent);}
                        else
                            dialog.cancel();

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



        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position+1) {

                    default:
                        System.out.println(userList.get(position).get("GroupName"));
                        //Log.d("Gflag",clusterDB.AmGroup(no));
                        Intent Cl = new Intent(getActivity(), GroupMembers.class);
                        Cl.putExtra("EventNo", EventNo);
                        Cl.putExtra("GroupName", userList.get(position).get("GroupName"));

                        startActivity(Cl);
                        break;
///here this onclick not working !!!!

                    // Nothing do!
                }


            }
    });

        return windows;
    }
    public void onRefresh() {
        syncSQLiteMySQLDB();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.refresh) {
//
//            syncSQLiteMySQLDB();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }




    public void syncSQLiteMySQLDB(){
        swipeRefreshLayout.setRefreshing(false);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        ArrayList<HashMap<String, String>> userList =  controller.getAllUsers(EventNo);
        if(userList.size()!=0){
            if(controller.dbSyncCount() != 0){
                prgDialog.show();
                params.put("usersJSON", controller.composeJSONfromSQLite());
                System.out.println(controller.composeJSONfromSQLite());
                client.post("http://carpe16.esy.es/carpe16/sync/insertusers.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        prgDialog.hide();
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length());
                            for(int i=0; i<arr.length();i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                System.out.println(obj.get("id"));
                                System.out.println(obj.get("status"));
                                controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString(),obj.get("Name").toString());
                            }
                            Toast.makeText(getActivity(), "DB Sync completed!", Toast.LENGTH_LONG).show();
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getActivity(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // TODO Auto-generated method stub
                        prgDialog.hide();
                        if(statusCode == 404){
                            Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }else if(statusCode == 500){
                            Toast.makeText(getActivity(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(getActivity(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getActivity(), "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();
        }
    }


}

