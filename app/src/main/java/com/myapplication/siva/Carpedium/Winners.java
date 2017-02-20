package com.myapplication.siva.Carpedium;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myapplication.siva.Carpedium.Databases.WinnnerDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Winners extends AppCompatActivity {

    HashMap<String, String> queryValues;

    List<String> cluster;
    List<String> events;
    List<String> place;
    String ClusterName;
    Spinner mspinner,rspinner;
    String EventName;

ProgressDialog prgDialog;
WinnnerDB winnnerDB;
    ArrayList<HashMap<String, String>> userList;

    private Toolbar toolbar;
    ListView placeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winners);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Winners");
        toolbar.setTitleTextColor(Color.WHITE);

        winnnerDB = new WinnnerDB(this);
        cluster = new ArrayList<String>();
        events = new ArrayList<String>();
        place = new ArrayList<String>();
//        getActionBar().setTitle("Winners");

        placeList = (ListView) findViewById(R.id.placelist);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        mspinner = (Spinner) findViewById(R.id.espinner);
        rspinner = (Spinner) findViewById(R.id.rspinner);

        final TextView etext = (TextView) findViewById(R.id.eText);
        final TextView rtext = (TextView) findViewById(R.id.rText);
       // winnnerDB.dropDB();
        mspinner.setVisibility(View.GONE);
        rspinner.setVisibility(View.GONE);
        etext.setVisibility(View.GONE);
        rtext.setVisibility(View.GONE);
       // winnnerDB.dropDB();
        final ArrayList<HashMap<String, String>> EventsList = winnnerDB.getAllwinners();

        if (EventsList.size() == 0) {
            prgDialog = new ProgressDialog(this);
            prgDialog.setMessage(" Please wait...Getting Results");
            prgDialog.setCancelable(false);
            syncSQLiteMySQLDB();
        }

     cluster = winnnerDB.getClusters();

       // List<String>  place = new ArrayList<String>();
        place.add("Choose Rank");
        place.add("Place");
        place.add("Position");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Winners.this, android.R.layout.simple_spinner_item, cluster);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        // attaching data adapter to spinner

        spinner.setAdapter(dataAdapter);



//*********************CLUSTER DROP DOWN****************
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position >= 0) {
                    // get spinner value
                    ClusterName=cluster.get(position);


                    events = winnnerDB.getEvents(ClusterName);
                    if(events.size()!=0){          // Creating adapter for spinner
                        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Winners.this, android.R.layout.simple_spinner_item, events);
                        // Drop down layout style - list view with radio button
                        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mspinner.setAdapter(dataAdapter1);}
                    System.out.println("Cluster "+cluster.get(position));



                    etext.setVisibility(View.VISIBLE);
                    mspinner.setVisibility(View.VISIBLE);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //*********************EVENTS DROP DOWN****************
        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position1, long id) {


                    System.out.println("Events " + events.get(position1));
                    EventName= events.get(position1);
                    //userList = winnnerDB.getsinglewinners(EventName);

                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(Winners.this, android.R.layout.simple_spinner_item, place);
                    // Drop down layout style - list view with radio button
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    rtext.setVisibility(View.VISIBLE);
                    rspinner.setVisibility(View.VISIBLE);
                    rspinner.setAdapter(dataAdapter2);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//*********************RANK DROP DOWN****************
        rspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position2, long id) {
                if (position2 > 0) {

                    switch (position2) {
                        case 1: {
                            listPlace();
                        }
                        break;
                        case 2: {
                            listPosition();
                        }
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

    }



    public void syncSQLiteMySQLDB() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        prgDialog.show();
       // params.put("Cluster",Clusternumber);
        // Log.d("event",Clustername);
        client.post("http://carpe16.esy.es/carpe16/sync/finaresults.php" , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                prgDialog.hide();
                updateSQLite(response);
                System.out.println(response);
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void updateSQLite(String response){
        //ArrayList<HashMap<String, String>> Eventsynclist;
       // Eventsynclist = new ArrayList<HashMap<String, String>>();
       // Gson gson = new GsonBuilder().create();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if(arr.length() != 0){
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                   // System.out.println(obj.get("EventId"));
                    System.out.println(obj.get("EventName"));
                    queryValues = new HashMap<String, String>();
                   // queryValues.put("EventId", obj.get("EventId").toString());
                    queryValues.put("EventName", obj.get("EventName").toString());
                    queryValues.put("EventNo", obj.get("EventNo").toString());
                    queryValues.put("Gflag", obj.get("Gflag").toString());
                    queryValues.put("userName", obj.get("Name").toString());
                    queryValues.put("EventNo", obj.get("EventNo").toString());
                    queryValues.put("ClusterName", obj.get("ClusterName").toString());
                    queryValues.put("GroupName", obj.get("GroupName").toString());

                    queryValues.put("Position", obj.get("Position").toString());
                    queryValues.put("Rank", obj.get("Rank").toString());
                    queryValues.put("RegiNo", obj.get("RegiNo").toString());

                    winnnerDB.insertUser(queryValues);

                }

                reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), Winners.class);
        startActivity(objIntent);
       // winnnerDB.dropDB();
        finish();
    }

//    public void listPlace() {
//        if (winnnerDB.gflag(EventName) == "0") {
//            //userList = null;
//            userList = winnnerDB.getsinglewinners(EventName);
//        }
//        if (winnnerDB.gflag(EventName) == "1") {  //ArrayList<HashMap<String, String>> userList;
//            //userList = null;
//            userList = winnnerDB.getGroup(EventName);
//        }
//        if (userList.size() != 0) {
//            ListAdapter adapter = new SimpleAdapter(getApplicationContext(), userList, R.layout.winner_list_item, new String[]{"Rank", "userName", "Regno"}, new int[]{R.id.place, R.id.name, R.id.regno});
//            placeList.setAdapter(adapter);
//            System.out.println(userList);
//        }
//
//    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "dropped db");
     winnnerDB.dropDB();
        finish();
    }



    public void listPosition()
    {
        userList =null;
        userList = winnnerDB.getAllGroupwinners(EventName);
        System.out.println(userList);
        //ArrayList<HashMap<String, String>> userList;
        ListAdapter adapter = new SimpleAdapter(getApplicationContext(), userList, R.layout.winner_list_item, new String[]{"Position", "userName","RegiNo"}, new int[]{R.id.place, R.id.name,R.id.regno});
        placeList.setAdapter(adapter);
    }


    public void listPlace()
    {
        userList =null;
        userList = winnnerDB.getsinglewinners(EventName);


        //ArrayList<HashMap<String, String>> userList;
        ListAdapter adapter = new SimpleAdapter(getApplicationContext(), userList, R.layout.winner_list_item, new String[]{"Rank", "userName","RegiNo"}, new int[]{R.id.place, R.id.name,R.id.regno});
        placeList.setAdapter(adapter);

    }




}

