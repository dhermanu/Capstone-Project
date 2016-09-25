package com.dhermanu.soulfull.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.activities.MainActivity;
import com.dhermanu.soulfull.activities.MapsActivity;
import com.dhermanu.soulfull.adapters.BusinessAdapter;
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
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
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
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private RecyclerView rvBusiness;
    private BusinessAdapter businessAdapter;
    private ArrayList<Business> businessListSaved = null;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    private double mLongitude;
    private double mLatitude;
    private static final int REQUEST_CODE = 1;


    TextView tittleText;

    @Override
    public void onLocationChanged(Location location) {

    }

    public interface FragmentCallBack{
        public void onTextUpdated(String text);
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
        updateList("San Francisco");

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
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    public void updateList(String location) {

        final String consumerKey = "";
        final String consumerSecret = "";
        final String token = "";
        final String tokenSecret = "";

        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        YelpAPI yelpAPI = apiFactory.createAPI();


        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", "halal");
        params.put("limit", "10");

        // locale params
        params.put("lang", "en");


        Call<SearchResponse> call = yelpAPI.search(location, params);


        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                // Update UI text with the searchResponse.

                SearchResponse searchResponse = response.body();
                ArrayList<Business> businessesList = searchResponse.businesses();
                businessAdapter = new BusinessAdapter(businessesList, getContext());
                rvBusiness.setAdapter(businessAdapter);


                businessesList = new ArrayList<>();
                for (Business business : businessesList) {
                    businessListSaved.add(business);
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };

        call.enqueue(callback);

    }

    public void updateListLat(double latitude, double longitude){
        final String consumerKey = "";
        final String consumerSecret = "";
        final String token = "";
        final String tokenSecret = "";

        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", "halal");
        params.put("limit", "10");

        // locale params
        params.put("lang", "en");

        CoordinateOptions coordinateOptions = CoordinateOptions.builder()
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
                businessAdapter = new BusinessAdapter(businessesList, getContext());
                rvBusiness.setAdapter(businessAdapter);


                businessesList = new ArrayList<>();
                for (Business business : businessesList) {
                    businessListSaved.add(business);
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };

        call.enqueue(callback);

    }

    protected synchronized void buildGoogleApiCLient(){
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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
            startActivity(new Intent(getActivity(), MapsActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            Toast.makeText(getContext(), "FAILED", Toast.LENGTH_SHORT).show();
            Log.v("TEST", "Connection-failed");
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(getContext(), String.valueOf(mLastLocation.getLatitude()), Toast.LENGTH_SHORT).show();
            Log.v("TEST-LATITUDE", String.valueOf(mLastLocation.getLatitude()));
            Log.v("TEST-LONGTITUDE", String.valueOf(mLastLocation.getLongitude()));
        }

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i("TEST", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("TEST", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

    }

    public void openAutoCompleteActivity(){
        try{
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (GooglePlayServicesRepairableException e){
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(), 0).show();

        }
        catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google play services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.v("TEST", (String) place.getAddress());
                updateList((String) place.getName());
                ((FragmentCallBack)getActivity()).onTextUpdated((String) place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
