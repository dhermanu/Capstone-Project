package com.dhermanu.soulfull;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.dhermanu.soulfull.adapters.BusinessAdapter;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Category;
import com.yelp.clientlib.entities.Coordinate;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autovalue.shaded.org.apache.commons.lang.ArrayUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dean on 9/24/16.
 */

public class Utility {
    private static BusinessAdapter businessAdapt;
    double searchLat;

    public static void updateList(String location, final Context context, final RecyclerView view) {

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
                Log.v("TEST", "SUCCESS");

                SearchResponse searchResponse = response.body();
                ArrayList<Business> businessesList = searchResponse.businesses();
                double searchLat = searchResponse.region().center().latitude();
                double searchLong = searchResponse.region().center().longitude();
                businessAdapt = new BusinessAdapter(businessesList, context, searchLat, searchLong);
                view.setAdapter(businessAdapt);

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
                Log.v("TEST", "FAILED");
            }
        };

        call.enqueue(callback);
    }

    public static void updateList(final double latitude, final double longitude,
                                  final Context context, final RecyclerView view) {
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
                businessAdapt = new BusinessAdapter(businessesList, context, latitude, longitude);
                view.setAdapter(businessAdapt);

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };

        call.enqueue(callback);
    }

    public static String distance(double lat1, double lon1, double lat2, double lon2) {
        DecimalFormat df = new DecimalFormat("0.#");
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);

        float result = (float) (results[0] / 1609.344);

        String distance = df.format(result) + " mi";

        return distance;
    }

    public static String convertImageURL(String url){
        char[] urlArray = url.toCharArray();
        urlArray[urlArray.length - 6] = 'l';
        urlArray = ArrayUtils.remove(urlArray, urlArray.length-5);


        return new String(urlArray);
    }

    public static String listToString(List<String> arrayList){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String s : arrayList)
        {
            sb.append(s);
            if(i != arrayList.size() - 1)
                sb.append(", ");
            i++;
        }
        return new String(sb);
    }

    public static String catListToString(List<Category> arrayList){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(Category category : arrayList){
            sb.append(category.name());
            if(i != arrayList.size() - 1)
                sb.append(", ");
            i++;
        }
        return new String(sb);
    }

}





