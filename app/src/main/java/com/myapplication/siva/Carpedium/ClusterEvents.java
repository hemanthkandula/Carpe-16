package com.myapplication.siva.Carpedium;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myapplication.siva.Carpedium.Databases.ClusterDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;


public class ClusterEvents extends AppCompatActivity  {
    ClusterDB clusterDB = new ClusterDB(this);
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;




//    final Bundle parameters = this.getIntent().getExtras();
//    String Clustername = parameters.getString("Cluster");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        String Clusternumber = null;
        Clusternumber = getIntent().getStringExtra("Cluster");


        setContentView(R.layout.activity_clusters);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(ClusterEvents.this, SpotRegistraion.class);
                startActivity(intent);
            }
        });


        getSupportActionBar().show();
        getSupportActionBar().setTitle("Carpe'16");
        final ListView events = (ListView)findViewById(R.id.Eventlist);


        final ArrayList<HashMap<String, String>> EventsList =  clusterDB.getAllEvents();


//        if(EventsList.size()!=0){
//            ListAdapter adapter = new SimpleAdapter( ClusterEvents.this,EventsList, R.layout.view_event_entry, new String[] { "EventId","EventName"}, new int[] {R.id.EventId, R.id.EventName});
//            //ListView myList=(ListView)findViewById(R.id.Eventlist);
//            events.setAdapter(adapter);
//            //Toast.makeText(getApplicationContext(), clusterDB.getSyncStatus(), Toast.LENGTH_LONG).show();
//        }

        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // your code
                // Toast.makeText(context,temparr.get(position),Toast.LENGTH_SHORT).show();
                    String no = Integer.toString(position+1);

                    switch( position )
                    {
//                        case 0:  Intent C1 = new Intent(ClusterEvents.this, studentsaddActivity.class);
//
//                            startActivity(C1);
//                            break;
//                        case 1:  Intent C2 = new Intent(ClusterEvents.this, studentsaddActivity.class);
//                            startActivity(C2);
//                            break;
//                        case 2:  Intent C3 = new Intent(ClusterEvents.this, studentsaddActivity.class);
//                            startActivity(C3);
//                            break;
//                        case 3:  Intent C4 = new Intent(ClusterEvents.this, studentsaddActivity.class);
//                            startActivity(C4);
//                            break;
//                        case 4:  Intent C5 = new Intent(ClusterEvents.this, studentsaddActivity.class);
//                            startActivity(C5);
//                            break;
//                        case 5:  Intent C6 = new Intent(ClusterEvents.this, studentsaddActivity.class);
//                            startActivity(C6);
//                            break;


                        default:
                            //Log.d("Gflag",clusterDB.AmGroup(no));
if (clusterDB.AmGroup(no).equals("1")){
                            Intent Cl = new Intent(ClusterEvents.this, GroupStudentsadd.class);
                            Cl.putExtra("EventNo",clusterDB.getEventNO(no));
                            System.out.println(clusterDB.getEventNO(no));
                            Log.d("Eventno",clusterDB.getEventNO(no));
                            startActivity(Cl);
                            break;}
else {
    Intent C2 = new Intent(ClusterEvents.this, studentsaddActivity.class);
    C2.putExtra("EventNo",clusterDB.getEventNO(no));
    System.out.println(clusterDB.getEventNO(no));
    Log.d("Eventno",clusterDB.getEventNO(no));
    startActivity(C2);
    break;
}

                            // Nothing do!
                    }



            }
        });






            if (EventsList.size() != 0) {
                // Set the User Array list in ListView
                ListAdapter adapter = new SimpleAdapter(ClusterEvents.this, EventsList, R.layout.view_event_entry, new String[] {
                        "EventId", "EventName" }, new int[] { R.id.EventId, R.id.EventName });

                events.setAdapter(adapter);
            }if (EventsList.size() == 0) {
            prgDialog = new ProgressDialog(this);
            prgDialog.setMessage(" Please wait...Getting Your Events");
            prgDialog.setCancelable(false);
            syncSQLiteMySQLDB(Clusternumber);
        }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id = item.getItemId();
//            if (id == R.id.refresh) {
//                syncSQLiteMySQLDB();
//                return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.winner)
        {
            Intent i = new Intent(this,Winners.class);
            startActivity(i);
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            Intent i = new Intent(this,ClusterEvents.class);
            startActivity(i);
            return true;
        } else if (id == R.id.aboutus) {

            Intent i = new Intent(this,AboutUs.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void syncSQLiteMySQLDB(String Clusternumber) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        prgDialog.show();
       params.put("Cluster",Clusternumber);
      // Log.d("event",Clustername);
       client.post("http://carpe16.esy.es/carpe16/sync/getusers.php" , params, new AsyncHttpResponseHandler() {
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
        ArrayList<HashMap<String, String>> Eventsynclist;
        Eventsynclist = new ArrayList<HashMap<String, String>>();
        Gson gson = new GsonBuilder().create();
        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if(arr.length() != 0){
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("EventId"));
                    System.out.println(obj.get("EventName"));
                    queryValues = new HashMap<String, String>();
                    queryValues.put("EventId", obj.get("EventId").toString());
                    queryValues.put("EventName", obj.get("EventName").toString());
                    queryValues.put("EventNo", obj.get("EventId").toString());
                    queryValues.put("Gflag", obj.get("Gflag").toString());


                    clusterDB.insertUser(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Id", obj.get("EventId").toString());
                    map.put("status", "1");
                    Eventsynclist.add(map);
                }
                updateMySQLSyncSts(gson.toJson(Eventsynclist));
                reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void updateMySQLSyncSts(String json) {
        System.out.println(json);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("syncsts", json);
        client.post("http://carpe16.esy.es/carpe16/sync/updatesyncsts.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), "WELCOME", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), ClusterEvents.class);
        startActivity(objIntent);
        finish();
    }
}





