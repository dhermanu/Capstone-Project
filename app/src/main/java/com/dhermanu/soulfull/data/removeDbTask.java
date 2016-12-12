package com.dhermanu.soulfull.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.dhermanu.soulfull.data.HalalContract.HalalEntry;
import com.dhermanu.soulfull.model.Halal;

/**
 * Created by dhermanu on 10/2/16.
 */

public class removeDbTask extends AsyncTask<Void, Void, Integer> {
    private static final String FAV_PREF = "favorite_halal";
    private final Context context;
    private final Halal halal;

    public removeDbTask(Context context, Halal halal) {
        this.context = context;
        this.halal = halal;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        SharedPreferences pref
                = context.getSharedPreferences(FAV_PREF, Context.MODE_PRIVATE);

        String key = halal.getId();
        SharedPreferences.Editor editList = pref.edit();
        editList.remove(key);
        editList.apply();

        return context.getContentResolver().delete(
                HalalEntry.CONTENT_URI,
                HalalEntry.COLUMN_ID + " = ?",
                new String[]{halal.getId()}
        );
    }
}
