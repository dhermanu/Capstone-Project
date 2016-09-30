package com.dhermanu.soulfull.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.dhermanu.soulfull.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.BubbleIconFactory;
import com.google.maps.android.ui.IconGenerator;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {

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
            return myContentsView;
        }
    }

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

        Log.v("TEST", "MOVED");
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.8696, 151.2094), 10));

        IconGenerator iconFactory = new IconGenerator(this);
        addIcon(iconFactory, "1", new LatLng(-33.8696, 151.2094));

        addIcon(iconFactory, "2", new LatLng(-33.9360, 151.2070));

        iconFactory.setStyle(IconGenerator.STYLE_RED);
        addIcon(iconFactory, "3", new LatLng(-33.8858, 151.096));

    }

    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new
                MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                title("TEST").
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        Marker marker = mMap.addMarker(markerOptions);

        //mMap.setOnMapLongClickListener(this);

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

}
