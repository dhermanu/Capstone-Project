package com.dhermanu.soulfull.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.dhermanu.soulfull.model.Halal;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import io.techery.properratingbar.ProperRatingBar;

/**
 * Created by dhermanu on 9/4/16.
 */
public class HalalAdapter extends RecyclerView.Adapter<HalalAdapter.ViewHolder> {


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

    public List<Halal> mHalal;
    public Context mContext;


    public HalalAdapter(List<Halal> halals, Context context){
        mHalal = halals;
        mContext = context;
    }

    @Override
    public HalalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate the costum layout
        View halalView = inflater.inflate(R.layout.list_item_business, parent , false);

        //return a new holder instance
        ViewHolder viewHolder = new ViewHolder(halalView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HalalAdapter.ViewHolder holder, int position) {
        final Halal halal = mHalal.get(position);

        TextView businessName = holder.businessName;
        TextView businessCategory = holder.businessCategory;
        TextView businessRating = holder.businessRating;
        ProperRatingBar properRatingBar = holder.properRatingBar;
        final ImageView businessImage = holder.businessImage;

        final String imageUrl = halal.getBackgroundPath();


        businessName.setText(halal.getName());
        businessCategory.setText(halal.getCategory());
        businessRating.setText(halal.getDistance());
        properRatingBar.setRating(halal.getRating());

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
        if(mHalal != null)
            return mHalal.size();

        return 0;
    }
}
