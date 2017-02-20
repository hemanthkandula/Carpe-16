package com.myapplication.siva.Carpedium;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.myapplication.siva.Carpedium.Databases.SqliteDB;

import java.util.ArrayList;
import java.util.HashMap;


public class GroupActivity extends Activity {
    //@Override
    String EventNo;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton2;
    ImageView win;
    public void onCreate(Bundle savedInstanceState)
    {
        HashMap<String, String> queryValues;

        EventNo = getIntent().getStringExtra("EventNo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupname);
        SqliteDB controller = new SqliteDB(this);
        System.out.println("Im a group");
        queryValues = new HashMap<String, String>();
        ArrayList<HashMap<String, String>> userList =  controller.getAllGroups(EventNo);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        ListView myList=(ListView)findViewById(android.R.id.list);
        //ditText Edittext = (EditText)findViewById(R.id.EditText);
        ListAdapter adapter =new SimpleAdapter( GroupActivity.this,userList, R.layout.view_user_entry, new String[] { "groupId","groupName"}, new int[] {R.id.gpId, R.id.gpName});
        myList.setAdapter(adapter);
        final CharSequence[] day_radio = {"First", "Second", "Third"};
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                win = (ImageView) view.findViewById(R.id.image_View);
                win.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String[] place = {""};
                        final String[] position1 = {""};


                        AlertDialog.Builder builder2 = new AlertDialog.Builder(GroupActivity.this);
                        builder2.setTitle("Choose Position");

                        builder2.setSingleChoiceItems(day_radio, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                                place[0] = day_radio[which] + "";
                            }
                        });

                        final EditText input = new EditText(getApplicationContext());
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        input.setTextColor(Color.BLACK);
                        builder2.setView(input);
                        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                position1[0] = input.getText().toString();

//dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });


                        AlertDialog alertdialog2 = builder2.create();
                        alertdialog2.show();
                    }


                });
//                Intent i7 = new Intent(getApplicationContext(),studentsaddActivity.class);
//                startActivity(i7);

            }

        });
    }






}