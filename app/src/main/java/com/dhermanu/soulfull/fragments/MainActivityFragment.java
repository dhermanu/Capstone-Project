package com.dhermanu.soulfull.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dhermanu.soulfull.BuildConfig;
import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.Utility;
import com.dhermanu.soulfull.activities.MainActivity;
import com.dhermanu.soulfull.activities.MapsActivity;
import com.dhermanu.soulfull.adapters.HalalAdapter;
import com.dhermanu.soulfull.model.Halal;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private RecyclerView rvBusiness;
    private HalalAdapter halalAdapter;
    private ArrayList<Halal> halalListSaved = null;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    double null_latlng = 999;
    private double longitude = null_latlng;
    private double latitude = null_latlng;
    boolean isConnected;
    String city = "San Francisco";

    private static final int REQUEST_CODE = 1;
    TextView tittleText;

    public interface FragmentCallBack {
        public void onTextUpdated(String text, boolean changeColor);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);


        rvBusiness = (RecyclerView) rootview.findViewById(R.id.recycler_business_list);
        rvBusiness.setLayoutManager(new LinearLayoutManager(getActivity()));

        tittleText = (TextView) getActivity().findViewById(R.id.locationTitle);
        setHasOptionsMenu(true);
        buildGoogleApiCLient();
        updateState(savedInstanceState);

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mapsIcon) {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            Parcelable listParcelable = Parcels.wrap(halalListSaved);
            Bundle bundle = new Bundle();
            bundle.putParcelable(MainActivity.EXTRA_DATA, listParcelable);
            bundle.putDouble(MainActivity.EXTRA_LAT, latitude);
            bundle.putDouble(MainActivity.EXTRA_LONG, longitude);
            bundle.putString(MainActivity.EXTRA_CITY, city);
            intent.putExtras(bundle);

            startActivity(intent);

            return true;
        }

        if (id == R.id.myLocationIcon) {
            if(mLastLocation != null){
                updateList(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                getContext(), rvBusiness);
                city = getString(R.string.current_location);
                ((FragmentCallBack)getActivity())
                        .onTextUpdated(getString(R.string.current_location), true);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable listParcelable = Parcels.wrap(halalListSaved);
        outState.putParcelable(MainActivity.EXTRA_SAVE, listParcelable);
        outState.putString(MainActivity.EXTRA_CITY, city);
        outState.putDouble(MainActivity.EXTRA_LONG, longitude);
        outState.putDouble(MainActivity.EXTRA_LAT, latitude);
    }

    protected void startLocationUpdates(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        if(mLastLocation != null){
            //Toast.makeText(getContext(), Double.toString(mLastLocation.getLatitude()), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.v(TAG, (String) place.getAddress());
                LatLng latLng = place.getLatLng();
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                updateList(latLng.latitude, latLng.longitude, getContext(), rvBusiness);
                ((FragmentCallBack)getActivity()).onTextUpdated((String) place.getName(), false);
                city = (String) place.getName();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    public void openAutoCompleteActivity(){
        try{
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (GooglePlayServicesRepairableException e){
            GoogleApiAvailability
                    .getInstance()
                    .getErrorDialog(getActivity(), e.getConnectionStatusCode(), 0).show();

        }
        catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google play services is not available: "
                    + GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void updateState(Bundle savedInstanceState){
        if(savedInstanceState != null)
        {
            halalListSaved = Parcels.unwrap(savedInstanceState.getParcelable(MainActivity.EXTRA_SAVE));
            city = savedInstanceState.getString(MainActivity.EXTRA_CITY);
            latitude = savedInstanceState.getDouble(MainActivity.EXTRA_LAT);
            longitude =savedInstanceState.getDouble(MainActivity.EXTRA_LONG);
            if(halalListSaved != null){
                halalAdapter = new HalalAdapter(halalListSaved, getContext());
                rvBusiness.setAdapter(halalAdapter);
            }
        }

        else{
            checkConnection(savedInstanceState);
        }
    }

    protected synchronized void buildGoogleApiCLient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void checkConnection(Bundle savedInstanceState){
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (savedInstanceState == null){
            // Run the initialize task service so that some stocks appear upon an empty database
            if (isConnected){

                if(latitude == null_latlng && longitude == null_latlng){
                    latitude = 37.7915153211144;
                    longitude = -122.39848567179;
                    city = getString(R.string.default_location);
                    ((FragmentCallBack)getActivity())
                            .onTextUpdated(getString(R.string.default_location), false);
                }

                else{
                    city = getString(R.string.current_location);
                    ((FragmentCallBack)getActivity())
                            .onTextUpdated(getString(R.string.current_location), true);
                }
                updateList(latitude, longitude, getContext(), rvBusiness);
            }

            if(!isConnected){
                new MaterialDialog.Builder(getContext())
                        .title(R.string.network_error_title)
                        .content(R.string.network_error_description)
                        .positiveText(R.string.network_error_close).show();
            }
        }
    }

    public void updateList(final double latitude, final double longitude,
                                              final Context context, final RecyclerView view) {
        final String term = "term";
        final String halal = "halal";
        final String limit = "limit";
        final String num_limit = "20";
        final String lang = "lang";
        final String lang_use = "en";


        YelpAPIFactory apiFactory = new YelpAPIFactory(
                BuildConfig.CONSUMER_KEY,
                BuildConfig.CONSUMER_SECRET,
                BuildConfig.TOKEN,
                BuildConfig.TOKEN_SECRET);

        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

        // general params
        params.put(term, halal);
        params.put(limit, num_limit);

        // locale params
        params.put(lang, lang_use);

        final CoordinateOptions coordinateOptions = CoordinateOptions.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        Call<SearchResponse> call = yelpAPI.search(coordinateOptions, params);


        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                // Update UI text with the searchResponse.
                SearchResponse searchResponse = response.body();
                ArrayList<Business> businessesList = searchResponse.businesses();
                ArrayList<Halal> halalArrayList = new ArrayList<>();

                for(Business business : businessesList){
                    Double rating = business.rating();
                    Halal halal = new Halal(business.name(), business.id());
                    if(business.imageUrl() != null){
                        halal.setBackgroundPath(Utility.convertImageURL(business.imageUrl()));
                    }
                    halal.setCategory(Utility.catListToString(business.categories()));
                    halal.setPhone(business.phone());
                    halal.setPhoneDisplay(business.displayPhone());
                    halal.setReview(business.snippetText());
                    halal.setRating(rating.intValue());
                    halal.setDisplayAddress(Utility.listToString(business.location().displayAddress()));
                    halal.setCity(business.location().city());
                    halal.setZip_code(business.location().postalCode());
                    halal.setState(business.location().stateCode());

                    if(business.location().neighborhoods() != null){
                        halal.setNeighbourhoods(Utility.listToString(business.location().neighborhoods()));
                    }

                    halal.setAddress(Utility.listToString(business.location().address()));
                    halal.setCoord_lat(business.location().coordinate().latitude());
                    halal.setCoord_long(business.location().coordinate().longitude());
                    halal.setDistance(Utility.distance(business.distance()));
                    halalArrayList.add(halal);
                }
                halalAdapter = new HalalAdapter(halalArrayList, context);
                halalListSaved = halalArrayList;
                view.setAdapter(halalAdapter);
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };

        call.enqueue(callback);
    }
}
