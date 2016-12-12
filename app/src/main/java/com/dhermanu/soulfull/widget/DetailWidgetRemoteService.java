package com.dhermanu.soulfull.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.data.HalalContract;
import com.dhermanu.soulfull.data.HalalContract.HalalEntry;


/**
 * Created by dhermanu on 10/3/16.
 */

public class DetailWidgetRemoteService extends RemoteViewsService {

    private static final String[] HALAL_COLUMNS = {
            HalalEntry.TABLE_NAME + "." + HalalEntry._ID,
            HalalEntry.COLUMN_ID,
            HalalEntry.COLUMN_NAME,
            HalalEntry.COLUMN_BACKGROUNDPATH,
            HalalEntry.COLUMN_CATEGORY,
            HalalEntry.COLUMN_PHONE,
            HalalEntry.COLUMN_DISPLAYPHONE,
            HalalEntry.COLUMN_REVIEW,
            HalalEntry.COLUMN_RATING,
            HalalEntry.COLUMN_ADDRESS,
            HalalEntry.COLUMN_DISPLAYADDRESS,
            HalalEntry.COLUMN_CITY,
            HalalEntry.COLUMN_COORD_LAT,
            HalalEntry.COLUMN_COORD_LONG
    };

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;
            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(HalalContract.HalalEntry.CONTENT_URI,
                        HALAL_COLUMNS,
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                int name_column = data.getColumnIndex(HalalContract.HalalEntry.COLUMN_NAME);
                int id_column = data.getColumnIndex(HalalContract.HalalEntry.COLUMN_ID);
                int city_column = data.getColumnIndex(HalalEntry.COLUMN_CITY);

                if(position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)){
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.list_halal_widget);

                views.setTextViewText(R.id.halal_business, data.getString(name_column));
                views.setTextViewText(R.id.halal_city, data.getString(city_column));

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra("set_sample",
                        data.getString(id_column));
                views.setOnClickFillInIntent(R.id.widget_list_halal, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.list_halal_widget);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                if(data.moveToPosition(i) && data != null){
                    return data.getLong(0);
                }
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
