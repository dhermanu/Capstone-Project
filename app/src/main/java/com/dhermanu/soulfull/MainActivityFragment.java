package com.dhermanu.soulfull;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView rvBusiness;
    private BusinessAdapter businessAdapter;
    private ArrayList<Business> businessListSaved = null;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        rvBusiness = (RecyclerView) rootview.findViewById(R.id.recycler_business_list);
        rvBusiness.setLayoutManager(new LinearLayoutManager(getActivity()));


        updateList();
        return rootview;
    }

    public void updateList(){

        final String consumerKey="";
        final String consumerSecret="";
        final String token="";
        final String tokenSecret="";

        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", "halal");
        params.put("limit", "10");

        // locale params
        params.put("lang", "en");
        Call<SearchResponse> call = yelpAPI.search("San Francisco", params);


        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                // Update UI text with the searchResponse.

                SearchResponse searchResponse = response.body();
                ArrayList<Business> businessesList =  searchResponse.businesses();
                businessAdapter = new BusinessAdapter(businessesList, getContext());
                rvBusiness.setAdapter(businessAdapter);


                businessesList = new ArrayList<>();
                for(Business business : businessesList){
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
}
