package com.dhermanu.soulfull.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.ShadeTransformation;
import com.dhermanu.soulfull.Utility;
import com.dhermanu.soulfull.activities.DetailActivity;
import com.dhermanu.soulfull.activities.MainActivity;
import com.dhermanu.soulfull.callbacks.ItemClickListener;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Category;

import java.util.List;

import autovalue.shaded.org.apache.commons.lang.ArrayUtils;

/**
 * Created by dhermanu on 9/4/16.
 */
public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView businessName, businessAddress, businessCategory, businessRating;
        public ImageView businessImage;
        private ItemClickListener itemClickListener;
        public ViewHolder(View itemView) {
            super(itemView);
            businessName = (TextView) itemView.findViewById(R.id.businessName);
            businessCategory = (TextView) itemView.findViewById(R.id.businessCategory);
            businessRating = (TextView) itemView.findViewById(R.id.businessRating);
            businessImage = (ImageView) itemView.findViewById(R.id.businessImage);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getPosition(), false);
        }
    }

    public List<Business> mBusiness;
    public Context mContext;
    public double mLatitude;
    public double mLongitude;

    public BusinessAdapter(List<Business> businesses, Context context
            , double latitude, double longitude){
        mBusiness = businesses;
        mContext = context;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    @Override
    public BusinessAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate the costum layout
        View repoView = inflater.inflate(R.layout.list_item_business, parent , false);

        //return a new holder instance
        ViewHolder viewHolder = new ViewHolder(repoView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BusinessAdapter.ViewHolder holder, int position) {
        final Business business = mBusiness.get(position);
        TextView businessName = holder.businessName;
        TextView businessCategory = holder.businessCategory;
        TextView businessRating = holder.businessRating;
        final ImageView businessImage = holder.businessImage;
        final String address = Utility.listToString(business.location().displayAddress());
        final String imageUrl = Utility.convertImageURL(business.imageUrl());
        String category = Utility.catListToString(business.categories());
        double businessLat = business.location().coordinate().latitude();
        double busienssLong = business.location().coordinate().longitude();
        String rating  = Double.toString(business.rating());
        businessName.setText(business.name());
        businessCategory.setText(category);


        businessRating.setText(Utility.distance(mLatitude, mLongitude, businessLat, busienssLong));


        Picasso
                .with(mContext)
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new ShadeTransformation((float) 0.5))
                .into(businessImage);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString(MainActivity.EXTRA_NAME, business.name());
                extras.putString(MainActivity.EXTRA_IMAGE_URL, imageUrl);
                extras.putString(MainActivity.EXTRA_DATA, business.displayPhone());
                extras.putString(MainActivity.EXTRA_DATA, business.phone());
                extras.putString(MainActivity.EXTRA_DATA, address);
                extras.putString(MainActivity.EXTRA_REVIEW, business.snippetText());

                intent.putExtras(extras);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mBusiness != null)
            return mBusiness.size();

        return 0;
    }
}
