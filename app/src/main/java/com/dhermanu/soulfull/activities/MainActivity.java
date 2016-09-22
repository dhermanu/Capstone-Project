package com.dhermanu.soulfull.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.fragments.MainActivityFragment;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.FragmentCallBack{
    public static String EXTRA_DATA = "com.extra.data";
    public static String EXTRA_NAME = "com.extra.name";
    public static String EXTRA_REVIEW = "com.extra.review";
    public static String EXTRA_IMAGE_URL = "com.extra.image_url";
    public static String EXTRA_PHONE_NUMBER = "com.extra.image_url";
    private static final int REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView tittleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(android.graphics.Color.BLACK);
        toolbar.setSubtitleTextColor(android.graphics.Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tittleText = (TextView) findViewById(R.id.locationTitle);

    }


    public void searchLocation(View view){
        MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content_main);
        fragment.openAutoCompleteActivity();

    }


    @Override
    public void onTextUpdated(String text) {
        tittleText.setText(text);
    }
}
