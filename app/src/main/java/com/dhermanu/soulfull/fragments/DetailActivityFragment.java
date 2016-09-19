package com.dhermanu.soulfull.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.activities.MainActivity;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private TextView businessReview;
    private ImageView imageBanner;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_detail, container, false);
        updateDetail(rootview);
        return rootview;
    }

    public void updateDetail(View rootView){
        Intent intent = getActivity().getIntent();
        Bundle args = intent.getExtras();
        CollapsingToolbarLayout collapsingToolbarLayout
                = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingtoolbar);

        collapsingToolbarLayout.setTitle(args.getString(MainActivity.EXTRA_NAME));
        businessReview = (TextView) rootView.findViewById(R.id.businessReview);
        businessReview.setText(args.getString(MainActivity.EXTRA_REVIEW));
        imageBanner = (ImageView) rootView.findViewById(R.id.imageBanner);
        Picasso.with(getActivity())
                .load(args.getString(MainActivity.EXTRA_IMAGE_URL))
                .fit()
                .centerCrop()
                .into(imageBanner);
    }
}
