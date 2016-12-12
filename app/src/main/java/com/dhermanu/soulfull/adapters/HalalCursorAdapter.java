package com.dhermanu.soulfull.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.ShadeTransformation;
import com.dhermanu.soulfull.activities.DetailActivity;
import com.dhermanu.soulfull.activities.MainActivity;
import com.dhermanu.soulfull.callbacks.ItemClickListener;
import com.dhermanu.soulfull.data.HalalContract;
import com.dhermanu.soulfull.model.Halal;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import io.techery.properratingbar.ProperRatingBar;

/**
 * Created by dhermanu on 10/3/16.
 */

public class HalalCursorAdapter extends RecyclerView.Adapter<HalalCursorAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate the costum layout
        View halalView = inflater.inflate(R.layout.list_item_business, parent , false);

        //return a new holder instance
        ViewHolder viewHolder = new ViewHolder(halalView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int name_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_NAME);
        int category_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_CATEGORY);
        int rating_column  = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_RATING);
        int background_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_BACKGROUNDPATH);
        int displayAddress_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_DISPLAYADDRESS);
        int displayPhone_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_DISPLAYPHONE);
        int review_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_DISPLAYADDRESS);
        int city_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_CITY);
        int id_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_ID);
        int lat_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_COORD_LAT);
        int long_column = mCursor.getColumnIndex(HalalContract.HalalEntry.COLUMN_COORD_LONG);

        String halal_name = mCursor.getString(name_column);
        String halal_category = mCursor.getString(category_column);
        int halal_rating = mCursor.getInt(rating_column);
        String halal_backgroundPath = mCursor.getString(background_column);
        String halal_displayAddress = mCursor.getString(displayAddress_column);
        String halal_displayPhone = mCursor.getString(displayPhone_column);
        String halal_review = mCursor.getString(review_column);
        String halal_id = mCursor.getString(id_column);
        double halal_lat = mCursor.getDouble(lat_column);
        double halal_long = mCursor.getDouble(long_column);

        TextView businessName = holder.businessName;
        TextView businessCategory = holder.businessCategory;
        TextView businessRating = holder.businessRating;
        ProperRatingBar properRatingBar = holder.properRatingBar;
        final ImageView businessImage = holder.businessImage;

        final String imageUrl = mCursor.getString(background_column);


        businessName.setText(mCursor.getString(name_column));
        businessCategory.setText(mCursor.getString(category_column));
        businessRating.setText(mCursor.getString(city_column));
        properRatingBar.setRating(mCursor.getInt(rating_column));

        Picasso
                .with(mContext)
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new ShadeTransformation((float) 0.5))
                .into(businessImage);

        final Halal halal = new Halal(halal_name, halal_id);
        halal.setCategory(halal_category);
        halal.setRating(halal_rating);
        halal.setBackgroundPath(halal_backgroundPath);
        halal.setDisplayAddress(halal_displayAddress);
        halal.setPhoneDisplay(halal_displayPhone);
        halal.setReview(halal_review);
        halal.setCoord_lat(halal_lat);
        halal.setCoord_long(halal_long);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                Parcelable data = Parcels.wrap(halal);
                Bundle bundle = new Bundle();
                bundle.putParcelable(MainActivity.EXTRA_DATA, data);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView businessName, businessAddress, businessCategory, businessRating;
        public ImageView businessImage;
        private ItemClickListener itemClickListener;
        ProperRatingBar properRatingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            businessName = (TextView) itemView.findViewById(R.id.businessName);
            businessCategory = (TextView) itemView.findViewById(R.id.businessCategory);
            businessRating = (TextView) itemView.findViewById(R.id.businessRating);
            businessImage = (ImageView) itemView.findViewById(R.id.businessImage);
            properRatingBar = (ProperRatingBar) itemView.findViewById(R.id.ratingBar);
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

    public Cursor mCursor;
    public Context mContext;

    public HalalCursorAdapter(Cursor cursor, Context context){
        mCursor = cursor;
        mContext = context;
    }
}
