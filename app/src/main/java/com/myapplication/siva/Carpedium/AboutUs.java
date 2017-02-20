package com.myapplication.siva.Carpedium;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setTitle("About Us");
    }

    public void onSiv(View view)
    {
        Uri uri = Uri.parse("https://in.linkedin.com/in/sivaskvs");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void onNachi(View view)
    {
        Uri uri = Uri.parse("https://in.linkedin.com/in/nachiappan-karuppiah-86058112b");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void onHemanth(View view)
    {
        Uri uri = Uri.parse("https://in.linkedin.com/in/hemanthkandula");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
    public void onReview(View view)
    {
        Uri uri = Uri.parse("https://goo.gl/forms/dBPmnEplJE5qvYNI2");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
