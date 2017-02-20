package com.myapplication.siva.Carpedium;

import android.app.*;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    public String EventNo;
    String GroupName;
   // SqliteDB controller = new SqliteDB(this);
    String TAG = "com.myapplication.siva.Carpedium";
    @Override
    public void onCreate(Bundle state) {

        EventNo = getIntent().getStringExtra("EventNo");

        GroupName = getIntent().getStringExtra("GroupName");


        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Registration Number");
        builder.setMessage(rawResult.getText());

        Log.d("Raw",rawResult.getText());

       // HashMap<String, String> queryValues = new HashMap<String, String>();
       // queryValues.put("userName", rawResult.getText());
        if (rawResult.getText() != null
                && rawResult.getText().trim().length() != 0) {
            //controller.insertUser(queryValues);
            this.callHomeActivity(rawResult.getText());
        } else {
            Toast.makeText(getApplicationContext(), "Please enter User name",
                    Toast.LENGTH_LONG).show();
        }



        builder.setNegativeButton("CANCEL", null);
        builder.show();
        Toast.makeText(this,rawResult.getText(),Toast.LENGTH_LONG);
        Toast.makeText(this,rawResult.getBarcodeFormat().toString(),Toast.LENGTH_LONG);


        Log.wtf(TAG, rawResult.getText()); // Prints scan results
        Log.wtf(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    public void callHomeActivity(String s) {

        if (GroupName == null){
        Intent objIntent = new Intent(getApplicationContext(),
                studentsaddActivity.class);

        objIntent.putExtra("regino",s);
        objIntent.putExtra("EventNo",EventNo);
        startActivity(objIntent);

        finish();}
        else    {
            Intent objIntent = new Intent(getApplicationContext(),
                    GroupMembers.class);

            objIntent.putExtra("regino",s);
            objIntent.putExtra("EventNo",EventNo);
            objIntent.putExtra("GroupName",GroupName);
            startActivity(objIntent);

            finish();}
    }
}