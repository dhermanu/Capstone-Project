package com.dhermanu.soulfull.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dhermanu.soulfull.R;

public class MainActivity extends AppCompatActivity {
    public static String EXTRA_DATA = "com.extra.data";
    public static String EXTRA_NAME = "com.extra.name";
    public static String EXTRA_REVIEW = "com.extra.review";
    public static String EXTRA_IMAGE_URL = "com.extra.image_url";
    public static String EXTRA_PHONE_NUMBER = "com.extra.image_url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.BLACK);
        toolbar.setSubtitleTextColor(android.graphics.Color.BLACK);
        setSupportActionBar(toolbar);
    }

}
