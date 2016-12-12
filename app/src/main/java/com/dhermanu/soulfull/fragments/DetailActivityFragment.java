package com.dhermanu.soulfull.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhermanu.soulfull.BuildConfig;
import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.ShadeTransformation;
import com.dhermanu.soulfull.Utility;
import com.dhermanu.soulfull.activities.MainActivity;
import com.dhermanu.soulfull.data.addDbTask;
import com.dhermanu.soulfull.data.removeDbTask;
import com.dhermanu.soulfull.model.Halal;
import com.dhermanu.soulfull.widget.appWidget;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import io.techery.properratingbar.ProperRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private FloatingActionButton fab;
    private static final String FAV_PREF = "favorite_halal";
    public final static String FAV_ADDED = "Business saved to favorites";
    public final static String FAV_REMOVED = "Business removed to favorites";
    private boolean mFavHalal = false;
    private Halal halal = null;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.favorite_fab);
        updateState(savedInstanceState, rootView);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable parcelable = Parcels.wrap(halal);
        outState.putParcelable(MainActivity.EXTRA_SAVE, parcelable);
    }
    public void updateState(Bundle savedInstanceState, View rootView){
        if(savedInstanceState != null)
        {
            halal = Parcels.unwrap(savedInstanceState.getParcelable(MainActivity.EXTRA_SAVE));
            if(halal != null){
                updateView(rootView, halal);
                bookmarkActivity(true);
            }
        }
        else{
            updateDetail(rootView);
        }
    }

    public void updateDetail(View rootView){

        if(getActivity().getIntent().getParcelableExtra(MainActivity.EXTRA_DATA) != null){
            halal = Parcels.unwrap(getActivity().getIntent().
                    getParcelableExtra(MainActivity.EXTRA_DATA));
            updateView(rootView, halal);
            bookmarkActivity(true);
        }

        else if(getActivity().getIntent().getStringExtra(MainActivity.EXTRA_MAPS)!= null){
            FetchHalalDetail(getActivity()
                    .getIntent()
                    .getStringExtra(MainActivity.EXTRA_MAPS),rootView);
            bookmarkActivity(false);

        }

    }

    public void bookmarkActivity(boolean flag){
        SharedPreferences sharedPreferences
                = getActivity().getSharedPreferences(FAV_PREF, Context.MODE_PRIVATE);

        if(flag && sharedPreferences.contains(halal.getId())){
            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
            mFavHalal = true;
        }

        //listener for the fav icon
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mFavHalal){
                    fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    Toast.makeText
                            (getActivity(), FAV_REMOVED, Toast.LENGTH_SHORT)
                            .show();

                    removeDbTask dbTask = new removeDbTask(getActivity(), halal);
                    dbTask.execute();
                    mFavHalal = false;

                    updateWidget();

                }

                else{
                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    Toast.makeText(getActivity(), FAV_ADDED, Toast.LENGTH_SHORT)
                            .show();
                    addDbTask dbTask = new addDbTask(getActivity(), halal);
                    dbTask.execute();
                    mFavHalal = true;

                    updateWidget();
                }
            }
        });
    }

    public void updateWidget() {
        AppWidgetManager man = AppWidgetManager.getInstance(getContext());
        int[] ids = man.getAppWidgetIds(
                new ComponentName(getActivity(), appWidget.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(appWidget.EXTRA_WIDGET_ID, ids);
        getContext().sendBroadcast(updateIntent);
    }

    public void updateView(View rootView, Halal halal){
        CollapsingToolbarLayout collapsingToolbarLayout
                = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingtoolbar);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(halal.getName());
        ((TextView) rootView.findViewById(R.id.businessReview)).setText(halal.getReview());
        ((TextView) rootView.findViewById(R.id.addressDisplay)).setText(halal.getDisplayAddress());
        ((TextView) rootView.findViewById(R.id.phoneDisplay)).setText(halal.getPhoneDisplay());
        ((TextView) rootView.findViewById(R.id.category)).setText(halal.getCategory());
        ((TextView) rootView.findViewById(R.id.distanceDisplay)).setText(halal.getDistance());
        ((ProperRatingBar) rootView.findViewById(R.id.ratingBar)).setRating(halal.getRating());

        ImageView imageBanner = (ImageView) rootView.findViewById(R.id.imageBanner);
        Picasso.with(getActivity())
                .load(halal.getBackgroundPath())
                .fit()
                .centerCrop()
                .transform(new ShadeTransformation((float) 0.2))
                .into(imageBanner);
    }

    public void FetchHalalDetail(final String businessId, final View rootView){
        final String lang = "lang";
        final String lang_use = "en";

        Map<String, String> params = new HashMap<>();
        params.put(lang, lang_use);

        YelpAPIFactory apiFactory = new YelpAPIFactory(
                BuildConfig.CONSUMER_KEY,
                BuildConfig.CONSUMER_SECRET,
                BuildConfig.TOKEN,
                BuildConfig.TOKEN_SECRET);

        YelpAPI yelpAPI = apiFactory.createAPI();
        Call<Business> call = yelpAPI.getBusiness(businessId, params);
        Callback<Business> callback = new Callback<Business>() {

            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                Business business = response.body();
                Double rating = business.rating();
                halal = new Halal(business.name(), business.id());
                halal.setBackgroundPath(Utility.convertImageURL(business.imageUrl()));
                halal.setCategory(Utility.catListToString(business.categories()));
                halal.setPhone(business.phone());
                halal.setPhoneDisplay(business.displayPhone());
                halal.setReview(business.snippetText());
                halal.setRating(rating.intValue());
                halal.setDisplayAddress(Utility.listToString(business.location().displayAddress()));
                halal.setCity(business.location().city());
                halal.setZip_code(business.location().postalCode());
                halal.setState(business.location().stateCode());
                halal.setAddress(Utility.listToString(business.location().address()));
                halal.setCoord_lat(business.location().coordinate().latitude());
                halal.setCoord_long(business.location().coordinate().longitude());

                SharedPreferences sharedPreferences
                        = getActivity().getSharedPreferences(FAV_PREF, Context.MODE_PRIVATE);
                if(sharedPreferences.contains(halal.getId())){
                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    mFavHalal = true;
                }

                updateView(rootView, halal);
            }
            @Override
            public void onFailure(Call<Business> call, Throwable t) {

            }
        };

        call.enqueue(callback);
    }
}
