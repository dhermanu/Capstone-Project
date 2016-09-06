package com.dhermanu.soulfull;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            businessAddress = (TextView) itemView.findViewById(R.id.businessAddress);
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

    public BusinessAdapter(List<Business> businesses, Context context){
        mBusiness = businesses;
        mContext = context;
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
        TextView businessAddress = holder.businessAddress;
        TextView businessCategory = holder.businessCategory;
        TextView businessRating = holder.businessRating;
        ImageView businessImage = holder.businessImage;

        businessName.setText(business.name());

        String address = listToString(business.location().displayAddress());
        businessAddress.setText(address);

        String category = catListToString(business.categories());
        businessCategory.setText(category);

        String rating  = Double.toString(business.rating());
        businessRating.setText(rating);

        Picasso
                .with(mContext)
                .load(convertImageURL(business.imageUrl()))
                .fit()
                .into(businessImage);

//        holder.setClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                ((CallbackTablet)mContext).onItemSelected(repo);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(mBusiness != null)
            return mBusiness.size();

        return 0;
    }

    public String listToString(List<String> arrayList){
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

    public String catListToString(List<Category> arrayList){
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

    public String convertImageURL(String url){
        char[] urlArray = url.toCharArray();
        urlArray[urlArray.length - 6] = 'l';
        urlArray = ArrayUtils.remove(urlArray, urlArray.length-5);


        return new String(urlArray);
    }
}
