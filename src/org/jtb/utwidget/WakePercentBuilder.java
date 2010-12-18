package org.jtb.utwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

public class WakePercentBuilder extends ViewBuilder {

	@Override
	RemoteViews build(Context context, int id) {
		Prefs prefs = new Prefs(context);
		Theme theme = prefs.getTheme(id);
		Mode mode = prefs.getMode(id);
		

		Layout layout = Layout.valueOf(mode, theme);
		Log.d("utwidget", "using layout: " + layout);

		int layoutId = layout.getLayoutId();
		RemoteViews updateViews = new RemoteViews(context.getPackageName(),
				layoutId);

		double wktime = SystemClock.uptimeMillis();
		//Log.d("utwidget", "wktime: " + wktime);
		double uptime = SystemClock.elapsedRealtime();
		//Log.d("utwidget", "uptime: " + uptime);
		double ratio = wktime / uptime;
		//Log.d("utwidget", "ratio: " + ratio);
		if (ratio >= 0.999f) {
			ratio = 0.999f;
		}
		int color = Color.parseColor("#00ff00");
		if (ratio > .75f) {
			color = Color.parseColor("#ff0000");
		} else if (ratio > .5f) {
			color = Color.parseColor("#ffff00");
		}
		
		String percent = String.format("%02.1f", ratio * 100) + "%";
		updateViews.setTextViewText(R.id.percent_text, percent);
		updateViews.setTextColor(R.id.percent_text, color);
		
		updateViews.setTextViewText(R.id.uptime_text, mode.getShortTitle(context));
	
		Intent i = UptimeWidget.getUpdateIntent(context, id);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		updateViews.setOnClickPendingIntent(R.id.widget, pi);
		
		return updateViews;
	}

}
