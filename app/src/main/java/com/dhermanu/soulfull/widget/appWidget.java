package com.dhermanu.soulfull.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.dhermanu.soulfull.R;
import com.dhermanu.soulfull.activities.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class appWidget extends AppWidgetProvider {
    public static final String EXTRA_WIDGET_ID ="com.dean.soulfull.widger";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
     //       Log.v("TEST", "UPDATE");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Set up the collection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
            }

            else {
                setRemoteAdapterV11(context, views);
            }

            Intent clickIntentTemplate = new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_halal_list, clickPendingIntentTemplate);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(EXTRA_WIDGET_ID)) {
    //        Log.v("TEST", "RECEIVE");
            int[] ids = intent.getExtras().getIntArray(EXTRA_WIDGET_ID);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        }
        else
            super.onReceive(context, intent);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_halal_list,
                new Intent(context, DetailWidgetRemoteService.class));
    //    Log.d("TEST", "setRemoteAdapter:");

    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_halal_list,
                new Intent(context,  DetailWidgetRemoteService.class));
    //    Log.d("TEST", "setRemoteAdapterV11:");
    }
}

