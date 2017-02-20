package com.myapplication.siva.Carpedium;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myapplication.siva.Carpedium.ClusterEvents;

public class MainActivity extends AppCompatActivity {



    //ProgressDialog prgDialog;
    ProgressDialog progressDialog;
    private static int retryConnectionNumber = 0;
    private final static int CONNECTION_RETRY_MAX = 5;
    private final static int REQUEST_TIMEOUT = 2000;
    public static final String UserPref = "Userpref";
    public static final String Regikey = "RegiKey";
    public static final String Passkey = "PassKey";
    //SharedPreferences settings;

    SharedPreferences prefs;



    EditText et1,et2;
    String regno,password;
    @Bind(R.id.reg)
    EditText reg;
    @Bind(R.id.pass)
    EditText pass;
    @Bind(R.id.login)
    Button login;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(!isNetworkConnected())
        {
            Toast.makeText(getApplicationContext(),":(  NO INTERNET :(",Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("..NO INTERNET..")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            Intent i = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        //Button startAnimation =(Button) findViewById(R.id.button1);
        final LinearLayout LoginBox = (LinearLayout) findViewById(R.id.LoginBox);

        LoginBox.setVisibility(View.GONE);
        /*startAnimation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
          */
        Animation animTranslate  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate);
        animTranslate.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) { }

            @Override
            public void onAnimationRepeat(Animation arg0) { }

            @Override
            public void onAnimationEnd(Animation arg0) {
                LoginBox.setVisibility(View.VISIBLE);
                Animation animFade  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade);
                LoginBox.startAnimation(animFade);
            }
        });
        ImageView imgLogo = (ImageView) findViewById(R.id.imageView1);
        imgLogo.startAnimation(animTranslate);

           /* }
        });*/
        prefs = getApplicationContext().getSharedPreferences(UserPref, Context.MODE_PRIVATE);


        // regno =getPreference(this,Regikey);
        // password =getPreference(this,Passkey);
        if (prefs.contains(Regikey)&&prefs.contains(Passkey))
        {
            regno = prefs.getString(Regikey, null);
            password = prefs.getString(Passkey,null);
            //Log.d("prefregi",regno);
            //Log.d("prefpass",password);

            reg.setText(regno);
            Log.d("getpref","I came get ");

            pass.setText(password);


        }



        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do your stuff here

                    login();
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                login();
            }
        });
    }

    private void login() {
        if (!validate()) {
            onLoginFailed();
        } else {


            regno = reg.getText().toString();
            password = pass.getText().toString();

//            setPreference(this,regno,Regikey);
//            setPreference(this,password,Passkey);



            login(regno, password);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void login(String regno, String password) {
        prefs = getApplicationContext().getSharedPreferences(UserPref, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Regikey, regno);
        editor.putString(Passkey,password);
        Boolean a = editor.commit();
        System.out.println("commit");
        System.out.println(a);
        class LoginAsync extends AsyncTask<String, String, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

            }
            @Override
            protected String doInBackground(String... params) {

                String regno = params[0];
                String password = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("RegNo", regno));
                nameValuePairs.add(new BasicNameValuePair("Password", password));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://carpe16.esy.es/carpe16/login.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                }catch ( Exception anyError) {
                    return null;
                }
//                catch (ClientProtocolException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                if(s != null && !s.equalsIgnoreCase("Failure")){
//                    SharedPreferences sharedpreferences = getSharedPreferences(UserPref, Context.MODE_PRIVATE);
//
//
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//
//                    editor.putString(Regikey, regno);
//                    editor.putString(Passkey, password);



                    progressDialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), ClusterEvents.class);
                    intent.putExtra("Cluster",s);
//Log.d("LoginClus",s);
                    finish();
                    startActivity(intent);
                }else if(s.equalsIgnoreCase("Failure"))
                {progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }

            }

        }

        LoginAsync la = new LoginAsync();
        la.execute(regno, password);

    }
//    static public void setPreference(Context c, String value, String key) {
//        SharedPreferences settings = c.getSharedPreferences(UserPref, 0);
//        //settings = c.getSharedPreferences(UserPref, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        Log.d("setpref","I came set ");
//
//        editor.putString(key, value);
//
//    }
//    static public String getPreference(Context c, String key) {
//
//        SharedPreferences settings = c.getSharedPreferences(UserPref, 0);
//        if(!settings.contains(key)){
//        settings = c.getSharedPreferences(UserPref, 0);
//        String value = settings.getString(key, "");
//            Log.d("getpref","I came get");
//
//            return value;}
//
//        else return null;
//    }



    private boolean validate() {
        boolean valid = true;
        String reg1 = reg.getText().toString();
        String pass1 = pass.getText().toString();
        if(reg1.isEmpty() || reg1.length() !=9)
        {
            reg.setError("Enter a valid reg number");
            valid = false;
        }
        else
        {
            reg.setError(null);
        }
        if(pass1.isEmpty())
        {
            pass.setError("Enter a valid password");
            valid = false;
        }
        else
        {
            pass.setError(null);
        }
        return valid;
    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        login.setEnabled(true);
    }
//    public void newFunc(View view)
//    {
//        Intent i = new Intent(this,SimpleScannerActivity.class);
//        startActivity(i);
//    }




}
