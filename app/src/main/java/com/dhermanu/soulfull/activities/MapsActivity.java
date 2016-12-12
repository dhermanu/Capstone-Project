package com.dhermanu.soulfull.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.model.Halal;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.techery.properratingbar.ProperRatingBar;

import static com.dhermanu.soulfull.R.id.map;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener {

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            this.myContentsView = getLayoutInflater().inflate(R.layout.marker_window, null);

        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView businessName = (TextView) myContentsView.findViewById(R.id.businessName);
            TextView businessAddress = (TextView) myContentsView.findViewById(R.id.businessAddress);
            ProperRatingBar properRatingBar = (ProperRatingBar) myContentsView.findViewById(R.id.ratingBar);
            TextView businessDistance = (TextView) myContentsView.findViewById(R.id.businessDistance);

            String data = marker.getSnippet();
            List<String> items = Arrays.asList(data.split("\\s*,\\s*"));
            int rating = Integer.parseInt(items.get(1));
            String miles = items.get(3);

            businessName.setText(items.get(0));
            businessAddress.setText(items.get(2));
            properRatingBar.setRating(rating);
            businessDistance.setText(miles);

            return myContentsView;
        }
    }

    private GoogleMap mMap;
    private ArrayList<Halal> halalList = null;
    private double latitude;
    private String searchResult;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        halalList = Parcels.unwrap(getIntent().getParcelableExtra(MainActivity.EXTRA_DATA));
        latitude = getIntent().getExtras().getDouble(MainActivity.EXTRA_LAT);
        longitude = getIntent().getExtras().getDouble(MainActivity.EXTRA_LONG);
        searchResult = getIntent().getExtras().getString(MainActivity.EXTRA_CITY);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

        View viewActionBar = getLayoutInflater().inflate(R.layout.layout_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText(searchResult);

        getSupportActionBar().setCustomView(viewActionBar, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapFragment.getMapAsync(this);
    }

//    public void upButton(View view){
//        onBackPressed();
//    }

//    @Override
//    public void onBackPressed() {
//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
            return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
        int padding = 100;

        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_RED);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        int i = 1;
        for (Halal halal : halalList) {
            addIcon(iconFactory,
                    Integer.toString(i),
                    new LatLng(halal.getCoord_lat(), halal.getCoord_long()),
                    halal);
            builder.include(new LatLng(halal.getCoord_lat(), halal.getCoord_long()));
            i++;
        }

        final LatLngBounds bounds = builder.build();
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cu);
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);


    }

    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position, Halal halal)
    {
        String data = dataBuilder(halal);
        MarkerOptions markerOptions = new
                MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV()).
                title(halal.getId()).
                snippet(data);

        mMap.addMarker(markerOptions);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent =  new Intent(this, DetailActivity.class);
        intent.putExtra(MainActivity.EXTRA_MAPS,marker.getTitle());
        startActivity(intent);
    }

    public String dataBuilder(Halal halal){
        StringBuilder sb = new StringBuilder();
        String commaSeparate = ",";

        sb.append(halal.getName());
        sb.append(commaSeparate);
        sb.append(halal.getRating());
        sb.append(commaSeparate);
        sb.append(halal.getAddress());
        sb.append(commaSeparate);
        sb.append(halal.getDistance());

        return new String(sb);
    }

}
