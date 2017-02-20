package com.myapplication.siva.Carpedium;

/**
 * Created by Siva Subramanian L on 03-10-2016.
 */

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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



public class Android extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    SqliteDB controller;
    ProgressDialog prgDialog;
    String EventNo;
    ListView myList;
    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton floatingActionButton1,floatingActionButton2,floatingActionButton3;
    String m_Text;
    HashMap<String, String> queryValues = new HashMap<String, String>();

    Context thiscontext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = container.getContext();

        View android = inflater.inflate(R.layout.android_frag, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) android.findViewById(R.id.swipe_refresh_layout);
   swipeRefreshLayout.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                syncSQLiteMySQLDB();
            }
        }
        );

        String regino = null;
        materialDesignFAM = (FloatingActionMenu) android.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) android.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) android.findViewById(R.id.material_design_floating_action_menu_item2);
        //floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) android.findViewById(R.id.material_design_floating_action_menu_item3);


        controller = new SqliteDB(getActivity());
        regino = getActivity().getIntent().getStringExtra("regino");
        EventNo = getActivity().getIntent().getStringExtra("EventNo");
        System.out.println(EventNo);
        if (regino != null) {

            queryValues.put("RegiNo", regino);
            queryValues.put("EventNo", EventNo);

            controller.insertUser(queryValues);


        }
        myList = (ListView) android.findViewById(R.id.studlist);

        ArrayList<HashMap<String, String>> userList = controller.getAllUsers(EventNo);

        if (userList.size() != 0) {
            ListAdapter adapter = new SimpleAdapter(thiscontext, userList, R.layout.swipeviewrowitem, new String[]{"updateStatus", "RegiNo"}, new int[]{R.id.use, R.id.mainText});
            myList.setAdapter(adapter);
            Toast.makeText(thiscontext, controller.getSyncStatus(), Toast.LENGTH_LONG).show();

        }
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage(" Please wait...");
        prgDialog.setCancelable(false);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB Action goes here
                Intent objIntent = new Intent(getActivity(), SimpleScannerActivity.class);
                objIntent.putExtra("EventNo", EventNo);


                startActivity(objIntent);
                getActivity().finish();
            }
        });
//        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // FAB Action goes here
//                syncSQLiteMySQLDB();
////                Intent i = new Intent(getActivity(),studentsaddActivity.class);
////                i.putExtra("EventNo", EventNo);
////
////                startActivity(i);
//
//
//            }
//        });
//

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB Action goes here
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter Registration Number");

                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if (m_Text != null) {

                            queryValues.put("RegiNo", m_Text);
                            queryValues.put("EventNo", EventNo);

                            controller.insertUser(queryValues);
                            dialog.cancel();
                        }

                        Intent objIntent = new Intent(getActivity(), studentsaddActivity.class);
//
                        objIntent.putExtra("EventNo", EventNo);
                       // objIntent.putExtra("RegiNo", m_Text);
                        startActivity(objIntent);
                        getActivity().finish();

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
        return android;

    }


    public void onRefresh() {
        syncSQLiteMySQLDB();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.refresh) {

            syncSQLiteMySQLDB();
            Intent i = new Intent(getActivity(),studentsaddActivity.class);

            startActivity(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }




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
                            Toast.makeText(getActivity(), "Sync completed!", Toast.LENGTH_LONG).show();

                            Intent objIntent = new Intent(getActivity(), studentsaddActivity.class);
//
                            objIntent.putExtra("EventNo", EventNo);
                           // objIntent.putExtra("RegiNo", m_Text);
                            startActivity(objIntent);
                            getActivity().finish();

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
                Toast.makeText(getActivity(), " in Sync!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getActivity(), "No data  please do enter User name  action", Toast.LENGTH_LONG).show();
        }
    }


}
