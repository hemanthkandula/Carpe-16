package com.myapplication.siva.Carpedium;

/**
 * Created by Siva Subramanian L on 03-10-2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myapplication.siva.Carpedium.Databases.ResulltsDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Iosone extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
    ResulltsDB resulltsDB;
    ProgressDialog prgDialog;
    String EventNo,Rank;
    private SwipeRefreshLayout swipeRefreshLayout;
    Context thiscontext;
    ArrayList<HashMap<String, String>> userList;
    ListView myList;
    ArrayList<String> listItems;
    ListViewCustomAdapter adapter1;
    ListAdapter adapter;
    SharedPreferences prefs;



    FloatingActionButton fab;
//    FloatingActionMenu materialDesignFAM;
//    com.github.clans.fab.FloatingActionButton floatingActionButton1,floatingActionButton2,floatingActionButton3;
String password;
    public static final String UserPref = "Userpref";
    public static final String Regikey = "RegiKey";
    public static final String Passkey = "PassKey";
    HashMap<String, String> queryValues = new HashMap<String, String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        thiscontext = container.getContext();
        resulltsDB = new ResulltsDB(thiscontext);
        thiscontext = container.getContext();
        //resulltsDB = new ResulltsDB(thiscontext);
        prgDialog = new ProgressDialog(thiscontext);
        prgDialog.setMessage("Transferring Data from Remote MySQL DB and Syncing SQLite. Please wait...");
        prgDialog.setCancelable(false);

        EventNo = getActivity().getIntent().getStringExtra("EventNo");


        View ios = inflater.inflate(R.layout.iosone_frag, container, false);
        //fab = (android.support.design.widget.FloatingActionButton) ios.findViewById(R.id.fab);
//        materialDesignFAM = (FloatingActionMenu) ios.findViewById(R.id.material_design_android_floating_action_menu);
//        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) ios.findViewById(R.id.material_design_floating_action_menu_item1);
//        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) ios.findViewById(R.id.material_design_floating_action_menu_item2);
//        floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) ios.findViewById(R.id.material_design_floating_action_menu_item3);
//
//

        fab = (FloatingActionButton)ios.findViewById(R.id.fab2);
        final CharSequence[] day_radio = {"None","First", "Second", "Third"};

        userList = resulltsDB.getAllGroupstuds(EventNo);
        System.out.println(userList);

        myList = (ListView) ios.findViewById(R.id.gotusers);
        swipeRefreshLayout = (SwipeRefreshLayout) ios.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener(){

                    @Override
                    public void onRefresh() {
                        Getonline();
                    }
                }
        );


        //adapter = new ListViewCustomAdapter(this, month, desc);


       // final CharSequence[] day_radio = {"None","First", "Second", "Third"};

        prefs = getActivity().getSharedPreferences(UserPref, Context.MODE_PRIVATE);



        if (prefs.contains(Regikey)&&prefs.contains(Passkey))
        {
            password = prefs.getString(Passkey,null);
        }
        //floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) ios.findViewById(R.id.fab) ;

//        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Click action
//                //resulltsDB.dropDB();
//                resulltsDB.dropDB();
//                Getonline();
//
//            }
//        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action


                UpdatingStudsToOnline();

            }
        });

        //myList.setAdapter(adapter);
        if (userList.size() != 0) {
            adapter = new SimpleAdapter(thiscontext, userList, R.layout.swipeviewrowitem, new String[]{"Rank", "GroupName"}, new int[]{R.id.use, R.id.mainText});
            myList.setAdapter(adapter);
            Toast.makeText(thiscontext, resulltsDB.getSyncStatus(), Toast.LENGTH_LONG).show();

        }
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Log.v("long clicked","pos: " + pos);
                switch (pos+1) {

                    default:
                        System.out.println(userList.get(pos).get("GroupName"));
                        //Log.d("Gflag",clusterDB.AmGroup(no));
                        Intent Cl = new Intent(getActivity(), GroupMembersresults.class);
                        Cl.putExtra("EventNo", EventNo);
                        Cl.putExtra("GroupName", userList.get(pos).get("GroupName"));
                        Cl.putExtra("Results", "I'm from results");


                        startActivity(Cl);
                        break;


                    // Nothing do!
                }

                return true;
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                final String[] place = {""};
                final String[] position1 = {""};
                Rank = "0";

                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setTitle("Choose Rank of group");

                builder2.setSingleChoiceItems(day_radio, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Rank = Integer.toString(which);
                    }
                });

                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextColor(Color.BLACK);
                builder2.setView(input);
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // position1[0] = input.getText().toString();








                        final AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
                        builder3.setTitle("ENTER PASSWORD");

                        final EditText input1 = new EditText(getActivity());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        input1.setTextColor(Color.BLACK);
                        builder3.setView(input1);
                        final TextView text = new TextView(getActivity());
                        text.setText("");
                        builder3.setView(input1);
                        builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(password.equals(input1.getText().toString()))
                                {
                                    text.setText("Password Ok");
                                    text.setTextColor(Color.GREEN);
                                    System.out.println("COrret pass");



                                    HashMap<String, String> User = userList.get(position);
                                    System.out.println( User.get("GroupName"));

//Log.d("");



                                    //String Reg = getregiNo(position+1);

                                    System.out.println(userList.get(position));
                                    //String Rank=

                                    //User.put("Position", ));
                                    User.put("Rank", Rank);
                                    User.put("EventNo", EventNo);

                                    resulltsDB.updateGroupRank(User);
                                    // User.put("RegiNo", );
                                    //userList = resulltsDB. getAllGroupstuds(EventNo);
                                   // myList.setAdapter(adapter);

                                    adapter = new SimpleAdapter(thiscontext, userList, R.layout.swipeviewrowitem, new String[]{"Rank", "GroupName"}, new int[]{R.id.use, R.id.mainText});
                                    myList.setAdapter(adapter);

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

//dismissing the dialog when the user makes a selection.
                        dialog.dismiss();
                    }
                });


                AlertDialog alertdialog2 = builder2.create();
                alertdialog2.show();
            }
        });

        return ios;
    }
    public void onRefresh() {
Getonline();    }

    public void getUsersFromOnline(String Clusternumber) {
        swipeRefreshLayout.setRefreshing(false);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        prgDialog.show();
        params.put("EventNo",Clusternumber);
        Log.d("event",Clusternumber);
        client.post("http://carpe16.esy.es/carpe16/sync/allusers.php" , params, new AsyncHttpResponseHandler() {
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
                    Toast.makeText(thiscontext, "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(thiscontext, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(thiscontext, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
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
                    System.out.println(obj.get("userName"));
                    //System.out.println(obj.get("RegNo"));
                    queryValues = new HashMap<String, String>();
                    queryValues.put("userName", obj.get("userName").toString());
                    queryValues.put("RegiNo", obj.get("RegiNo").toString());
                    queryValues.put("EventNo", obj.get("EventNo").toString());
                    queryValues.put("GroupName", obj.get("GroupName").toString());

                    queryValues.put("Position", obj.get("Position").toString());
                    queryValues.put("Rank", obj.get("Rank").toString());


                    resulltsDB.insertUser(queryValues);
//                    HashMap<String, String> map = new HashMap<String, String>();
//                    map.put("Id", obj.get("EventId").toString());
//                    map.put("status", "1");
                    //Eventsynclist.add(map);
                }
                // updateMySQLSyncSts(gson.toJson(Eventsynclist));
                swipeRefreshLayout.setRefreshing(false);

                userList =resulltsDB.getAllGroupstuds(EventNo);
                adapter = new SimpleAdapter(thiscontext, userList, R.layout.swipeviewrowitem, new String[]{"Rank", "GroupName"}, new int[]{R.id.use, R.id.mainText});

                myList.setAdapter(adapter);

               // reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void Getonline(){
        ArrayList<HashMap<String, String>> userList = resulltsDB.getAllstuds(EventNo);
resulltsDB.dropDB();
        userList =null;
//resulltsDB.onUpgrade(resulltsDB,1,2);


            getUsersFromOnline(EventNo);

    }


    public void reloadActivity() {
        Intent objIntent = new Intent(thiscontext,GroupStudentsadd.class);
        objIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        objIntent.putExtra("EventNo",EventNo);

        //objIntent.putExtra("GroupName",Group);

        startActivity(objIntent);
        getActivity().finish();
    }


    public void UpdatingStudsToOnline(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        ArrayList<HashMap<String, String>> userList =  resulltsDB.getAllstuds(EventNo);
        if(userList.size()!=0){
            if(resulltsDB.dbSyncCount() != 0){
                prgDialog.show();
                params.put("usersJSON", resulltsDB.composeJSONfromSQLite());
                System.out.println(resulltsDB.composeJSONfromSQLite());
                client.post("http://carpe16.esy.es/carpe16/sync/updateuser.php",params ,new AsyncHttpResponseHandler() {
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
                                resulltsDB.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
                            }
                            Toast.makeText(getActivity(), "DB Sync completed!", Toast.LENGTH_LONG).show();
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


//    private class MyListAdapter extends ArrayAdapter<String>{
//        private int layout;
//        public MyListAdapter(Context context, int resource, List<String> objects) {
//            super(context, resource, objects);
//            layout=resource;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ViewHolder mainViewHolder = null;
//            if(convertView == null){
//                LayoutInflater inflater = LayoutInflater.from(getContext());
//                convertView = inflater.inflate(layout,parent,false);
//                ViewHolder viewHolder = new ViewHolder();
//                viewHolder.mainText = (TextView)convertView.findViewById(R.id.mainText);
//                viewHolder.use = (TextView)convertView.findViewById(R.id.use);
//                viewHolder.button = (Button) convertView.findViewById(R.id.button);
//                viewHolder.button.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(getActivity(),GroupMembers.class);
//                        i.putExtra("Results",GroupName);
//
//                        startActivity(i);
//                    }
//                });
//                convertView.setTag(viewHolder);
//
//            }
//            else{
//                mainViewHolder = (ViewHolder) convertView.getTag();
//                mainViewHolder.mainText.setText(getItem(position));
//                mainViewHolder.mainText.setText(getItem(position));
//            }
//
//            return super.getView(position, convertView, parent);
//        }
//    }
//
//    public class ViewHolder{
//        TextView mainText,use;
//        Button button;
//
//    }
//
//
//
}